from scripts.utils import load_config

_base_ = [
    '../mmdetection/configs/_base_/models/cascade_mask_rcnn_r50_fpn.py',
    '../mmdetection/configs/_base_/datasets/coco_instance.py',
    '../mmdetection/configs/_base_/schedules/schedule_1x.py',
    '../mmdetection/configs/_base_/default_runtime.py'
]

model = dict(
    type='CascadeRCNN',
    backbone=dict(
        type='ResNet50',
        depth=50,
        num_stages=4,
        out_indices=(0, 1, 2, 3),
        frozen_stages=1,
        norm_cfg=dict(type='BN', requires_grad=True),
        norm_eval=True,
        style='pytorch'),
    neck=dict(
        type='FPN',
        in_channels=[256, 512, 1024, 2048],
        out_channels=256,
        num_outs=5),
    rpn_head=dict(
        type='RPNHead',
        in_channels=256,
        feat_channels=256,
        anchor_generator=dict(
            type='AnchorGenerator',
            scales=[8],
            ratios=[0.5, 1.0, 2.0],
            strides=[4, 8, 16, 32, 64]),
        bbox_coder=dict(
            type='DeltaXYWHBBoxCoder',
            target_means=[0.0, 0.0, 0.0, 0.0],
            target_stds=[1.0, 1.0, 1.0, 1.0]),
        loss_cls=dict(
            type='CrossEntropyLoss', use_sigmoid=True, loss_weight=1.0),
        loss_bbox=dict(type='L1Loss', loss_weight=1.0)),
    roi_head=dict(
        type='StandardRoIHead',
        bbox_roi_extractor=dict(
            type='SingleRoIExtractor',
            roi_layer=dict(type='RoIAlign', output_size=7, sampling_ratio=0),
            out_channels=256,
            featmap_strides=[4, 8, 16, 32]),
        bbox_head=dict(
            type='Shared2FCBBoxHead',
            in_channels=256,
            fc_out_channels=1024,
            roi_feat_size=7,
            num_classes=499,
            bbox_coder=dict(
                type='DeltaXYWHBBoxCoder',
                target_means=[0.0, 0.0, 0.0, 0.0],
                target_stds=[0.1, 0.1, 0.2, 0.2]),
            loss_cls=dict(type='CrossEntropyLoss', loss_weight=1.0),
            loss_bbox=dict(type='L1Loss', loss_weight=1.0))
    )
)

img_norm_cfg = dict(
    mean=[123.675, 116.28, 103.53],
    std=[58.395, 57.12, 57.375],
    to_rgb=True
)

img_scales = [(480, 480), (640, 640), (800, 800), (960, 960)]

train_pipeline = [
    dict(type='LoadImageFromFile'),
    dict(type='LoadAnnotations', with_bbox=True, with_mask=True),
    dict(
        type='Resize',
        img_scale=img_scales,
        multiscale_mode='value',
        keep_ratio=True
    ),
    dict(type='RandomFlip', flip_ratio=0.5),
    dict(type='Normalize', **img_norm_cfg),
    dict(type='Pad', size_divisor=32),
    dict(type='DefaultFormatBundle'),
    dict(type='Collect', keys=['img', 'gt_bboxes', 'gt_labels', 'gt_masks']),
]

config = load_config('configs/config.yaml')

if config['data']['use_subsets']:
    train_data_path = config['subsets']['train']
    val_data_path = config['subsets']['val']
    test_data_path = config['subsets']['test']
else:
    train_data_path = config['data']['train']
    val_data_path = config['data']['val']
    test_data_path = config['data']['test']

data = dict(
    samples_per_gpu=2,
    workers_per_gpu=2,
    train=dict(
        type='CocoDataset',
        ann_file=train_data_path + '/annotation.json',
        img_prefix=train_data_path + '/images/',
        pipeline=train_pipeline
    ),
    val=dict(
        type='CocoDataset',
        ann_file=val_data_path + '/annotation.json',
        img_prefix=val_data_path + '/images/',
        pipeline=train_pipeline
    ),
    test=dict(
        type='CocoDataset',
        ann_file=test_data_path + '/annotation.json',
        img_prefix=test_data_path + '/images/',
        pipeline=train_pipeline
    )
)

train_cfg = dict(
    sgd=dict(lr=0.02, momentum=0.9, weight_decay=0.0001),
    lr_config=dict(policy='step', step=[8, 11]),
    warmup='linear',
    warmup_iters=500,
    warmup_ratio=0.001
)

log_config = dict(
    interval=50,
    hooks=[dict(type='TextLoggerHook'), dict(type='TensorboardLoggerHook')]
)

checkpoint_config = dict(interval=1, save_best='auto')

work_dir = '../models'
load_from = '../mmdetection/checkpoints/pretrained_model.pth'
