//
//  DetailPageViewController.h
//  iOS UI Test
//
//  Created by Aaban Tariq Murtaza on 19/05/2015.
//  Copyright (c) 2015 AppJon. All rights reserved.
//

#import <UIKit/UIKit.h>

#import <CoreGraphics/CoreGraphics.h>
#import <ImageIO/ImageIO.h>
#import "MCOMessageView.h"


//@class MCOMessageView;
//@class MCOIMAPAsyncSession;
//@class MCOMAPMessage;
@protocol addToReadProtocol <NSObject>

-(void) addToReadListWebService :(MCOIMAPMessage *) message;
-(void) addToFavList:(MCOIMAPMessage *)message sender :(UIButton *)sender;
-(void) removeFromFavList :(MCOIMAPMessage *)message sender :(UIButton *)sender;
-(void) reloadCellAtIndexPath :(NSIndexPath *) indexPath;

@end

@interface DetailPageViewController : UIViewController<MCOMessageViewDelegate>{
    IBOutlet MCOMessageView * _messageView;
    NSMutableDictionary * _storage;
    NSMutableSet * _pending;
    NSMutableArray * _ops;
    MCOIMAPSession * _session;
    MCOIMAPMessage * _message;
    NSMutableDictionary * _callbacks;
    NSString * _folder;
}

@property (nonatomic, copy) NSString * folder;


@property (weak, nonatomic) id<addToReadProtocol> emailListVCRef;
@property (strong, nonatomic) NSIndexPath * indexPath;
@property (nonatomic, strong) MCOIMAPSession * session;
@property (nonatomic, strong) MCOIMAPMessage * message;
@property (nonatomic, strong) NSMutableDictionary *messagePreviews;
@property (nonatomic, strong) MCOIMAPMessageRenderingOperation * messageRenderingOperation;
@property (weak, nonatomic) IBOutlet UIActivityIndicatorView *activityIndicator;
@property (weak, nonatomic) IBOutlet UILabel *dateLabel;

@property (weak, nonatomic) IBOutlet UIButton *favBtn;
- (IBAction)favBtnPressed:(id)sender;


@property (strong, nonatomic) NSString *messageBody;
@property (strong, nonatomic) NSString *messageHeader;

@property (weak, nonatomic) IBOutlet UILabel *cirlceLabel;
@property (weak, nonatomic) IBOutlet UILabel *headerLabel;

@property (weak, nonatomic) IBOutlet UITextView *bodyTextView;

@end
