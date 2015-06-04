//
//  NIDropDown.h
//  NIDropDown
//
//  Created by Bijesh N on 12/28/12.
//  Copyright (c) 2012 Nitor Infotech. All rights reserved.
//

#import <UIKit/UIKit.h>
//#import "NavSelectorCollectionViewCell.h"
//#import "ThemeFactory.h"

@class NIDropDown;
@protocol NIDropDownDelegate
- (void) niDropDownDelegateMethod: (NIDropDown *) sender;
- (void) didSelectFromDropDown: (NSIndexPath *) indexPath;

@end

@interface NIDropDown : UIView <UITableViewDelegate, UITableViewDataSource>
{
    NSString *animationDirection;
    UIImageView *imgView;
    CGFloat yCoordinate;
    NSIndexPath *indexPath;
    CGFloat xCoordinate;
    UIView *parentView;
}

@property (nonatomic, strong) UITableView *table;
@property (nonatomic, retain) id <NIDropDownDelegate> delegate;
@property (nonatomic, retain) NSString *animationDirection;
@property (nonatomic) BOOL isOpened;

- (void) hideDropDown;


- (id)showDropDown:(UIView *)b xOffSet:(CGFloat)xOffSet yCoordinate:(CGFloat )yCoordinateVal height:(CGFloat *)height dataArray:(NSArray *)arr imageArray:(NSArray *)imgArr direction:(NSString *)direction view:(UIView *)parentView;



@end
