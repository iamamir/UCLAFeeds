//
//  NIDropDown.m
//  NIDropDown
//
//  Created by Bijesh N on 12/28/12.
//  Copyright (c) 2012 Nitor Infotech. All rights reserved.
//

#import "NIDropDown.h"
#import "QuartzCore/QuartzCore.h"

@interface NIDropDown ()

@property(nonatomic, strong) UIView *btnView;
@property(nonatomic, retain) NSArray *list;
@property(nonatomic, retain) NSArray *imageList;
@end

@implementation NIDropDown

@synthesize table;
@synthesize btnView;
@synthesize list;
@synthesize imageList;
@synthesize delegate;
@synthesize animationDirection;


- (id)showDropDown:(UIView *)b xOffSet:(CGFloat )xOffSet yCoordinate:(CGFloat )yCoordinateVal height:(CGFloat *)height dataArray:(NSArray *)arr imageArray:(NSArray *)imgArr direction:(NSString *)direction view:(UIView *)parentViewVal{
    parentView = parentViewVal;
    
    yCoordinate = yCoordinateVal + b.frame.size.height + b.frame.origin.y;
    xCoordinate = b.frame.origin.x - xOffSet + 4;
    btnView = b;
    animationDirection = direction;
    [self setBackgroundColor:[UIColor clearColor]];
    self.table = (UITableView *)[super init];
    if (self) {
        // Initialization code
        CGRect btn = btnView.frame;
        self.list = [NSArray arrayWithArray:arr];
        self.imageList = [NSArray arrayWithArray:imgArr];
        self.frame = CGRectMake(xCoordinate, yCoordinate, btn.size.width, 0);
        self.layer.shadowOffset = CGSizeMake(-5, 5);
        
        self.layer.masksToBounds = NO;
//        self.layer.cornerRadius = 8;
//        self.layer.shadowRadius = 5;
        self.layer.shadowOpacity = 0.5;
        self.layer.shadowOffset = CGSizeMake(2.0, 2.0);
        
        table = [[UITableView alloc] initWithFrame:CGRectMake(0, 0, btn.size.width, 0)];
        table.delegate = self;
        table.dataSource = self;
//        table.layer.cornerRadius = 5;
        table.backgroundColor = [UIColor colorWithRed:0.17 green:0.29 blue:0.37 alpha:1];
        
        [table setSeparatorInset:UIEdgeInsetsMake(0, 0, 10, 15)];
        table.separatorStyle = UITableViewCellSeparatorStyleNone;
        [table setScrollEnabled:NO];
        table.separatorColor = [UIColor whiteColor];
        
        [UIView beginAnimations:nil context:nil];
        [UIView setAnimationDuration:0.25];
        
        
        self.frame = CGRectMake(xCoordinate, yCoordinate, btn.size.width, *height);
        
        table.frame = CGRectMake(0, 0, btn.size.width, *height);
        [UIView commitAnimations];
        [parentView.superview bringSubviewToFront:parentView];
        [parentView addSubview:self];
        [self addSubview:table];
    }
    self.isOpened = YES;
    return self;
}

-(void)hideDropDown {
    CGRect btn = btnView.frame;



    [UIView animateWithDuration:0.25 animations:^{
        self.frame = CGRectMake(xCoordinate, yCoordinate, btn.size.width, 0);
        table.frame = CGRectMake(0, 0, btn.size.width, 0);
        self.isOpened = NO;
    } completion:^(BOOL finished) {
        [parentView.superview sendSubviewToBack:parentView];
        [self removeFromSuperview];
    }];



//    self.frame = CGRectMake(xCoordinate, yCoordinate, btn.size.width, 0);
//    table.frame = CGRectMake(0, 0, btn.size.width, 0);
//    self.isOpened = NO;
//    [parentView.superview sendSubviewToBack:parentView];
//    [self removeFromSuperview];

    
/*
    [UIView beginAnimations:nil context:nil];
    [UIView setAnimationDuration:0.25];
    
    self.frame = CGRectMake(xCoordinate, yCoordinate, btn.size.width, 0);
    table.frame = CGRectMake(0, 0, btn.size.width, 0);
    self.isOpened = NO;
    
    [UIView commitAnimations];
    
    [parentView.superview sendSubviewToBack:parentView];
    [self removeFromSuperview];
 */


}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 40;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return [self.list count];
}


- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPathVal {
    static NSString *CellIdentifier = @"Cell";
    
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:CellIdentifier];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:CellIdentifier];
        cell.textLabel.font = [UIFont systemFontOfSize:15];
        
        cell.textLabel.textAlignment = NSTextAlignmentCenter;
        cell.textLabel.textColor = [UIColor whiteColor];
        [cell setBackgroundColor:[UIColor colorWithRed:17/255.0 green:29/255.0 blue:37/255.0 alpha:1]];
//        [cell setBackgroundColor:[UIColor clearColor]];
        
        UIView * v = [[UIView alloc] init];
        v.backgroundColor = [UIColor grayColor];
        cell.selectedBackgroundView = v;
    }

    cell.textLabel.text =[list objectAtIndex:indexPathVal.row];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPathVal {
    [self.delegate didSelectFromDropDown:indexPathVal];
}

- (void) myDelegate {
//    [self.delegate niDropDownDelegateMethod:self];
}

-(void)dealloc {
}

@end
