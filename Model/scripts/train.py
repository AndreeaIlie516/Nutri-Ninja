# import os
import sys
# import mmcv

# absolute_path = os.path.abspath('../mmdetection')
# import sys
sys.path.append('../mmdetection')

from mmdetection.mmdet.apis import train_detector

print("If this prints, the import worked!")
# sys.path.append(absolute_path)
# from mmcv import Config
# from mmdet.datasets import build_dataset
# from mmdet.models import build_detector
# from mmdet.apis import train_detector, set_random_seed
# from mmcv.runner import load_checkpoint
#
# cfg_path = '../configs/cascade_rcnn_config.py'
#
# cfg = Config.fromfile(cfg_path)
#
# # Setup the dataset
# datasets = [build_dataset(cfg.data.train)]
#
# # Build the model
# model = build_detector(cfg.model, train_cfg=cfg.train_cfg, test_cfg=cfg.test_cfg)
#
# # Load pre-trained weights
# if cfg.get('load_from'):
#     load_checkpoint(model, cfg.load_from, map_location='cpu')
#
# # Set up directories
# mmcv.mkdir_or_exist(os.path.abspath(cfg.work_dir))
#
# # Initialize the logger for the training process
# log_file = os.path.join(cfg.work_dir, 'train.log')
# logger = mmcv.get_root_logger(log_file=log_file, log_level=cfg.log_level)
#
# # Train the model
# train_detector(model, datasets, cfg, distributed=False, validate=True)
#
# # Add hooks for monitoring and logging (Tensorboard, etc.)
# if cfg.log_config.hooks:
#     from mmcv.runner import HOOKS, Hook
#     hook_cfgs = cfg.log_config.hooks
#     hooks = []
#     for hook_cfg in hook_cfgs:
#         assert isinstance(hook_cfg, dict) and 'type' in hook_cfg
#         hook_type = hook_cfg.pop('type')
#         if hook_type not in HOOKS:
#             raise ValueError(f'{hook_type} is not registered in hook registry')
#         hooks.append(HOOKS.get(hook_type)(**hook_cfg))
#     model.register_hook(hooks)
