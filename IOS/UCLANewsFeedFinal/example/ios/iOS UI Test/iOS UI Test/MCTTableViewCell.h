//
//  MCTTableViewCell.h
//  iOS UI Test
//
//  Created by Paul Young on 14/07/2013.
//  Copyright (c) 2013 AppJon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <MailCore/MailCore.h>

@protocol ManageFavDelegate <NSObject>

-(void) addToFavList:(MCOIMAPMessage *)message sender :(UIButton *)sender;
-(void) addToReadList:(MCOIMAPMessage *)message sender :(UIButton *)sender;
-(void) removeFromFavList :(MCOIMAPMessage *)message sender :(UIButton *)sender;
-(void) reloadCellAtIndexPath :(NSIndexPath *) indexPath;

@end

@interface MCTTableViewCell : UITableViewCell

-(void) makeCellUnread;
-(void) makeCellRead;

@property (nonatomic, strong) MCOIMAPMessageRenderingOperation * messageRenderingOperation;

@property (strong, nonatomic) MCOIMAPMessage *message;
@property (strong, nonatomic) NSIndexPath * indexPath;
@property (weak, nonatomic) IBOutlet UILabel *bigSubjectLbl;
@property (weak, nonatomic) IBOutlet UILabel *detailLbl;
@property (weak, nonatomic) IBOutlet UILabel *cirleLbl;
@property (weak, nonatomic) id<ManageFavDelegate> delegate;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *activityIndicator;
@property (weak, nonatomic) IBOutlet UIButton *favBtn;

- (IBAction)favBtnPressed:(id)sender;

@end
