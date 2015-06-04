//
//  BackgroundView.h
//  mDas
//
//  Created by Aaban Tariq Murtaza on 12/5/14.
//  Copyright (c) 2014 Itchy Fingerz. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol dismissPopoverProtocol <NSObject>

-(void) dismisPopover;

@end

@interface BackgroundView : UIView

@property (nonatomic, weak) id<dismissPopoverProtocol> delegate;

@end
