//
//  MasterViewController.h
//  iOS UI Test
//
//  Created by Aaban Tariq on 4/8/13.
//  Copyright (c) 2013 AppJon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NIDropDown.h"
#import "DetailPageViewController.h"
#import "BackgroundView.h"
#import "MCTTableViewCell.h"
#import "LoadMoreCell.h"

#define UsernameKey @"username"
#define userIdKey @"userID"
#define messageIDKey @"messageID"
#define isFavouriteKey @"isFav"
#define isReadKey @"isRead"


@protocol destroyInstanceDelegate <NSObject>

-(void) destroyMaster;

@end

@interface MasterViewController : UIViewController <UITableViewDataSource, UITableViewDelegate, UISearchBarDelegate,dismissPopoverProtocol, NIDropDownDelegate, ManageFavDelegate, addToReadProtocol>{
    BOOL searchBool;
    BOOL readBool;
    BOOL unreadBool;
    BOOL allBool;
    BOOL favBool;
    BOOL shouldAnimate;
    NSInteger belowMessageNumber;
}

@property (weak, nonatomic) id<destroyInstanceDelegate> delegate;
@property (weak, nonatomic) IBOutlet UIButton *favBtn;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) NIDropDown *dropDown;
-(void) addToReadListWebService :(MCOIMAPMessage *) message;
@property (weak, nonatomic) IBOutlet BackgroundView *touchView;
- (IBAction)dropDown:(id)sender;


- (IBAction)logout:(id)sender;

- (void) startOAuth2;

@end
