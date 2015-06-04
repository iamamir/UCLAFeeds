//
//  BackgroundView.m
//  mDas
//
//  Created by Aaban Tariq Murtaza on 12/5/14.
//  Copyright (c) 2014 Itchy Fingerz. All rights reserved.
//

#import "BackgroundView.h"

@implementation BackgroundView

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect {
    // Drawing code
}
*/

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
    [self.delegate dismisPopover];
}


@end
