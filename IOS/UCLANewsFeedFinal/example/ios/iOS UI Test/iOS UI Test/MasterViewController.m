//
//  MasterViewController.m
//  iOS UI Test
//
//  Created by Aaban Tariq on 4/8/13.
//  Copyright (c) 2013 AppJon. All rights reserved.
//

#import "MasterViewController.h"
#import <MailCore/MailCore.h>
#import "FXKeychain.h"
#import "MCTMsgViewController.h"
#import "GTMOAuth2ViewControllerTouch.h"
#import "UIView+Toast.h"
#import "SettingsViewController.h"

#import "MBProgressHUD.h"

#define URL_POST_MESSAGES @"http://10.14.9.4:9000/postMessages"
#define URL_FETCH_READ_MESSAGES @"http://10.14.9.4:9000/getReadMessages"
#define URL_FETCH_FAV_MESSAGES @"http://10.14.9.4:9000/getFavouriteMessages"

#define CLIENT_ID @"191445719766-uejoijg6ikte6h7qoit30clih9vkdr29.apps.googleusercontent.com"
#define CLIENT_SECRET @"GtA_QL1BfcnJLU090NZZq2AO"
#define KEYCHAIN_ITEM_NAME @"MailCore OAuth 2.0 Token"

#define NUMBER_OF_MESSAGES_TO_LOAD		10


static NSString *mailCellIdentifier = @"MailCell";
static NSString *inboxInfoIdentifier = @"InboxStatusCell";

@interface MasterViewController ()
@property (nonatomic, strong) NSArray *messages;
@property (nonatomic, strong) NSMutableArray *favEmails;
@property (nonatomic, strong) NSMutableArray *filteredEmails;
@property (nonatomic, strong) NSMutableArray *readEmails;
@property (nonatomic, strong) NSMutableArray *unreadEmails;


@property (nonatomic, strong) MCOIMAPOperation *imapCheckOp;
@property (nonatomic, strong) MCOIMAPSession *imapSession;
@property (nonatomic, strong) MCOIMAPFetchMessagesOperation *imapMessagesFetchOp;


@property (nonatomic) NSInteger totalNumberOfInboxMessages;
@property (nonatomic) BOOL isLoading;
@property (nonatomic, strong) UIActivityIndicatorView *loadMoreActivityView;
@property (nonatomic, strong) NSMutableDictionary *messagePreviews;
@end

@implementation MasterViewController

- (void)viewDidLoad {
	[super viewDidLoad];
	
//	[self.tableView registerClass:[MCTTableViewCell class]
//		   forCellReuseIdentifier:mailCellIdentifier];

	self.loadMoreActivityView =
	[[UIActivityIndicatorView alloc]
	 initWithActivityIndicatorStyle:UIActivityIndicatorViewStyleGray];
	
	[[NSUserDefaults standardUserDefaults] registerDefaults:@{ HostnameKey: @"imap.gmail.com" }];
    [self startOAuth2];
    [self setupDropDown];
    
    self.filteredEmails = [[NSMutableArray alloc] init];
    self.readEmails = [[NSMutableArray alloc] init];
    self.unreadEmails = [[NSMutableArray alloc] init];
    self.favEmails = [[NSMutableArray alloc] init];
    
    self.touchView.delegate = self;
}

-(void)viewWillAppear:(BOOL)animated{
    if (shouldAnimate){
        shouldAnimate = NO;
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    }
}

-(void) setupDropDown{
    self.dropDown = [[NIDropDown alloc] init];
    [self.dropDown setDelegate:self];
    [self.dropDown setIsOpened:NO];
}

-(void)addToFavList:(MCOIMAPMessage *)message sender :(UIButton *)sender{
    [self addToFavListWebService:message sender:sender];
}

-(void)addToReadList:(MCOIMAPMessage *)message sender :(UIButton *)sender{
    [self addToReadListWebService:message];
}


-(void) removeFromFavListWebService :(MCOIMAPMessage *) message sender :(UIButton *)sender{
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    config.HTTPAdditionalHeaders = @{ @"Content-Type"  : @"application/json"};
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config delegate:nil delegateQueue:nil];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL_POST_MESSAGES]];
    request.HTTPMethod = @"POST";
    
    NSMutableDictionary *dictionary = [[NSMutableDictionary alloc] init];
    
    [dictionary setObject:[[NSUserDefaults standardUserDefaults] objectForKey:UsernameKey] forKey:userIdKey];
    [dictionary setObject:[NSString stringWithFormat:@"%d", message.uid] forKey:messageIDKey];
    [dictionary setObject:@"false" forKey:isFavouriteKey];
    
    if ([message.isRead boolValue]){
        [dictionary setObject:@"true" forKey:isReadKey];
    }else{
        [dictionary setObject:@"false" forKey:isReadKey];
    }
    
    NSData *jsonDict=[NSJSONSerialization dataWithJSONObject:dictionary options:0 error:nil];
    [request setHTTPBody:jsonDict];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    });

    
    [[session uploadTaskWithRequest:request fromData:jsonDict completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
        });
        if (error){
            
            dispatch_async(dispatch_get_main_queue(), ^{
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Service Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                [alertView show];
            });

        }else{
            NSDictionary* json = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
            NSLog(@"Data %@", json);
            
            if ([[json objectForKey:@"statusCode"] integerValue] == 200){
                [sender setImage:[UIImage imageNamed:@"newsFavIconUnselected"] forState:UIControlStateNormal];
                message.isFav = [NSNumber numberWithBool:NO];
                [self.favEmails removeObject:message];
                
                if ([message.isFav boolValue]){
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [self.view makeToast:@"Added to favourites"];
                    });
                }else{
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [self.view makeToast:@"Removed from favourites"];
                        [self.tableView reloadData];
                    });
                }

                
            }else{
                dispatch_async(dispatch_get_main_queue(), ^{
                    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Service Error" delegate:nil cancelButtonTitle:@"OK" otherButtonTitles:nil];
                    [alertView show];
                });
            }
        }
    }] resume];
}


-(void) addToFavListWebService :(MCOIMAPMessage *)message sender :(UIButton *)sender{
    
    NSSortDescriptor *sort =
    [NSSortDescriptor sortDescriptorWithKey:@"header.date" ascending:NO];

    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    config.HTTPAdditionalHeaders = @{ @"Content-Type"  : @"application/json"};
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config delegate:nil delegateQueue:nil];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL_POST_MESSAGES]];
    request.HTTPMethod = @"POST";
    
    NSMutableDictionary *dictionary = [[NSMutableDictionary alloc] init];
    
    [dictionary setObject:[[NSUserDefaults standardUserDefaults] objectForKey:UsernameKey] forKey:userIdKey];
    [dictionary setObject:[NSString stringWithFormat:@"%d", message.uid] forKey:messageIDKey];
    
    
    if ([message.isFav boolValue]){
        [dictionary setObject:@"true" forKey:isFavouriteKey];
    }else{
        [dictionary setObject:@"false" forKey:isFavouriteKey];
    }
    
    if ([message.isRead boolValue]){
        [dictionary setObject:@"true" forKey:isReadKey];
    }else{
        [dictionary setObject:@"false" forKey:isReadKey];
    }

    NSData *jsonDict=[NSJSONSerialization dataWithJSONObject:dictionary options:0 error:nil];
    [request setHTTPBody:jsonDict];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    });
    
    
    [[session uploadTaskWithRequest:request fromData:jsonDict completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
        });
        
        if (error){
            dispatch_async(dispatch_get_main_queue(), ^{
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Service Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                [alertView show];
            });
        }else{
            NSDictionary* json = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
            NSLog(@"Data %@", json);
            
            if ([[json objectForKey:@"statusCode"] integerValue] == 200){
                [sender setImage:[UIImage imageNamed:@"newsFavIconSelected"] forState:UIControlStateNormal];
                message.isFav = [NSNumber numberWithBool:YES];
                [self.favEmails addObject:message];
                
                NSArray *temp = [self.favEmails sortedArrayUsingDescriptors:@[sort]];
                self.favEmails = [NSMutableArray arrayWithArray:temp];

                
                if ([message.isFav boolValue]){
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [self.view makeToast:@"Added to favourites"];
                    });
                }else{
                    dispatch_async(dispatch_get_main_queue(), ^{
                        [self.view makeToast:@"Removed from favourites"];
                    });
                }
                
            }else{
                dispatch_async(dispatch_get_main_queue(), ^{
                    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Service Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                    [alertView show];
                });
            }
        }
    }] resume];
}


-(void) addToReadListWebService :(MCOIMAPMessage *) message{
    
    NSSortDescriptor *sort =
    [NSSortDescriptor sortDescriptorWithKey:@"header.date" ascending:NO];

    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    config.HTTPAdditionalHeaders = @{ @"Content-Type"  : @"application/json"};
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config delegate:nil delegateQueue:nil];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL_POST_MESSAGES]];
    request.HTTPMethod = @"POST";
    
    NSMutableDictionary *dictionary = [[NSMutableDictionary alloc] init];
    
    [dictionary setObject:[[NSUserDefaults standardUserDefaults] objectForKey:UsernameKey] forKey:userIdKey];
    [dictionary setObject:[NSString stringWithFormat:@"%d", message.uid] forKey:messageIDKey];
    
    if ([message.isFav boolValue]){
        [dictionary setObject:@"true" forKey:isFavouriteKey];
    }else{
        [dictionary setObject:@"false" forKey:isFavouriteKey];
    }

    if ([message.isRead boolValue]){
        [dictionary setObject:@"true" forKey:isReadKey];
    }else{
        [dictionary setObject:@"false" forKey:isReadKey];
    }
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    });
    NSData *jsonDict=[NSJSONSerialization dataWithJSONObject:dictionary options:0 error:nil];
    [request setHTTPBody:jsonDict];
    
    [[session uploadTaskWithRequest:request fromData:jsonDict completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
        });
        if (error){
            UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Service Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
            [alertView show];
        }else{
            NSDictionary* json = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
            NSLog(@"Data %@", json);
            if ([[json objectForKey:@"statusCode"] integerValue] == 200){
                message.isRead = [NSNumber numberWithBool:YES];
                [self.readEmails addObject:message];
                [self.unreadEmails removeObject:message];
                NSArray *temp = [self.readEmails sortedArrayUsingDescriptors:@[sort]];
                self.readEmails = [NSMutableArray arrayWithArray:temp];
                temp = [self.unreadEmails sortedArrayUsingDescriptors:@[sort]];
                self.unreadEmails = [NSMutableArray arrayWithArray:temp];
                

                
            }else{
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Service Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                [alertView show];
            }
        }
    }] resume];
}


-(void) fetchFavMessages{
    NSSortDescriptor *sort =
    [NSSortDescriptor sortDescriptorWithKey:@"header.date" ascending:NO];

    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    config.HTTPAdditionalHeaders = @{ @"Content-Type"  : @"application/json"};
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config delegate:nil delegateQueue:nil];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL_FETCH_FAV_MESSAGES]];
    request.HTTPMethod = @"POST";
    
    NSMutableDictionary *dictionary = [[NSMutableDictionary alloc] init];
    
    [dictionary setObject:[[NSUserDefaults standardUserDefaults] objectForKey:UsernameKey] forKey:userIdKey];
    
    NSData *jsonDict=[NSJSONSerialization dataWithJSONObject:dictionary options:0 error:nil];
    [request setHTTPBody:jsonDict];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    });
    [[session uploadTaskWithRequest:request fromData:jsonDict completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        
        dispatch_async(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
        });
        
        if (error){
            UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Service Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
            [alertView show];
        }else{
            NSDictionary* json = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
            NSLog(@"Data %@", json);
            
            if ([[json objectForKey:@"statusCode"] integerValue] == 200){
                if ([[json objectForKey:@"data"] objectForKey:@"favourites"] != [NSNull null]){
                    for (NSDictionary * dict in [[json objectForKey:@"data"] objectForKey:@"favourites"]) {
                        for (MCOIMAPMessage *msg in self.messages) {
                            if (msg.uid == [[dict objectForKey:@"messageID"] integerValue]){
                                msg.isFav = [NSNumber numberWithBool:YES];
                                [self.favEmails addObject:msg];
                                break;
                            }
                        }
                    }
                    self.favEmails = [[self.favEmails sortedArrayUsingDescriptors:@[sort]] mutableCopy];
                }
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    [self.tableView reloadData];
                });


                
            }else{
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Service Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                [alertView show];
            }
        }
    }] resume];
}

- (void) fetchReadAndFavMessageUIDs{
    
    NSSortDescriptor *sort =
    [NSSortDescriptor sortDescriptorWithKey:@"header.date" ascending:NO];

    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    config.HTTPAdditionalHeaders = @{ @"Content-Type"  : @"application/json"};
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config delegate:nil delegateQueue:nil];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URL_FETCH_READ_MESSAGES]];
    request.HTTPMethod = @"POST";
    
    NSMutableDictionary *dictionary = [[NSMutableDictionary alloc] init];
    
    [dictionary setObject:[[NSUserDefaults standardUserDefaults] objectForKey:UsernameKey] forKey:userIdKey];
    
    NSData *jsonDict=[NSJSONSerialization dataWithJSONObject:dictionary options:0 error:nil];
    [request setHTTPBody:jsonDict];
    dispatch_async(dispatch_get_main_queue(), ^{
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    });
    
    [[session uploadTaskWithRequest:request fromData:jsonDict completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
        });
        
        if (error){
            UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Service Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
            [alertView show];
        }else{
            NSDictionary* json = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
            NSLog(@"Data %@", json);
            
            if ([[json objectForKey:@"statusCode"] integerValue] == 200){
                
                if ([[json objectForKey:@"data"] objectForKey:@"isReadList"] != [NSNull null]){
                    for (NSDictionary * dict in [[json objectForKey:@"data"] objectForKey:@"isReadList"]) {
                        for (MCOIMAPMessage *msg in self.messages) {
                            if (msg.uid == [[dict objectForKey:@"messageID"] integerValue]){
                                msg.isRead = [NSNumber numberWithBool:YES];
                                if (![self.readEmails containsObject:msg]){
                                    [self.readEmails addObject:msg];
                                }
                                [self.unreadEmails removeObject:msg];
                                
                                break;
                            }else{
                                msg.isRead = [NSNumber numberWithBool:NO];
                                if (![self.unreadEmails containsObject:msg]){
                                    [self.unreadEmails addObject:msg];
                                }
                                [self.readEmails removeObject:msg];
                            }
                        }
                    }
                    
                    self.unreadEmails = [[self.unreadEmails sortedArrayUsingDescriptors:@[sort]] mutableCopy];
                    self.readEmails = [[self.readEmails sortedArrayUsingDescriptors:@[sort]] mutableCopy];
                }
                
                [self fetchFavMessages];
            }else{
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Service Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                [alertView show];
            }
        }
    }] resume];
}

- (void) setMessageArrays{
   
    [self fetchReadAndFavMessageUIDs];
}


-(void)removeFromFavList:(MCOIMAPMessage *)message sender:(UIButton *)sender{
    [self removeFromFavListWebService:message sender:sender];
}

-(void)dismisPopover{
    if ([self isAlreadyOpened]){
        [self.dropDown hideDropDown];
    }
}

-(BOOL ) isAlreadyOpened{
    if (self.dropDown.isOpened) {
        return YES;
    }else{
        return NO;
    }
}

//-(void)searchBarCancelButtonClicked:(UISearchBar *)searchBar{
//    searchBool = NO;
//    [self.filteredEmails removeAllObjects];
//    [self.tableView reloadData];
//}

- (void) startLogin
{
	NSString *username = [[NSUserDefaults standardUserDefaults] objectForKey:UsernameKey];
	NSString *password = [[FXKeychain defaultKeychain] objectForKey:PasswordKey];
	NSString *hostname = [[NSUserDefaults standardUserDefaults] objectForKey:HostnameKey];
    
    if (!username.length || !password.length) {
        [self performSelector:@selector(showSettingsViewController:) withObject:nil afterDelay:0.5];
        return;
    }

	[self loadAccountWithUsername:username password:password hostname:hostname oauth2Token:nil];
}

- (IBAction)dropDown:(id)sender {
    if (![self isAlreadyOpened]){
        CGFloat f = 120;
        CGFloat yCoordinate = self.touchView.frame.origin.y + 7;
        
        NSArray *data = [NSArray arrayWithObjects:@"All", @"Read", @"Unread", nil];
        [self.dropDown showDropDown:sender xOffSet:0 yCoordinate:yCoordinate height:&f dataArray:data imageArray:[NSArray array] direction:@"" view:self.touchView];
        [self.view bringSubviewToFront:self.touchView];
    }else{
        [self.dropDown hideDropDown];
    }
}

- (IBAction)logout:(id)sender {
    [self.navigationController popViewControllerAnimated:NO];
    [self.delegate destroyMaster];
}

- (void) startOAuth2
{
    GTMOAuth2Authentication * auth = [GTMOAuth2ViewControllerTouch authForGoogleFromKeychainForName:KEYCHAIN_ITEM_NAME
                                                                                           clientID:CLIENT_ID
                                                                                       clientSecret:CLIENT_SECRET];
    
    //    if ([auth refreshToken] == nil) {
    
    
    
    MasterViewController * __weak weakSelf = self;
    GTMOAuth2ViewControllerTouch *viewController = [GTMOAuth2ViewControllerTouch controllerWithScope:@"https://mail.google.com/"
                                                                                            clientID:CLIENT_ID
                                                                                        clientSecret:CLIENT_SECRET
                                                                                    keychainItemName:KEYCHAIN_ITEM_NAME
                                                                                   completionHandler:^(GTMOAuth2ViewControllerTouch *viewController, GTMOAuth2Authentication *retrievedAuth, NSError *error) {
                                                                                       [weakSelf loadWithAuth:retrievedAuth];
                                                                                   }];
    shouldAnimate = YES;
    [self.navigationController pushViewController:viewController
                                         animated:YES];
    //    }
    //    else {
    //        [auth beginTokenFetchWithDelegate:self
    //                        didFinishSelector:@selector(auth:finishedRefreshWithFetcher:error:)];
    //    }
}

- (void)auth:(GTMOAuth2Authentication *)auth
finishedRefreshWithFetcher:(GTMHTTPFetcher *)fetcher
       error:(NSError *)error {
    [self loadWithAuth:auth];
}

- (void)loadWithAuth:(GTMOAuth2Authentication *)auth
{
	NSString *hostname = [[NSUserDefaults standardUserDefaults] objectForKey:HostnameKey];
	[self loadAccountWithUsername:[auth userEmail] password:nil hostname:hostname oauth2Token:[auth accessToken]];
}

- (void)loadAccountWithUsername:(NSString *)username
                       password:(NSString *)password
                       hostname:(NSString *)hostname
                    oauth2Token:(NSString *)oauth2Token
{
	self.imapSession = [[MCOIMAPSession alloc] init];
	self.imapSession.hostname = hostname;
	self.imapSession.port = 993;
	self.imapSession.username = username;
	self.imapSession.password = password;
    if (oauth2Token != nil) {
        self.imapSession.OAuth2Token = oauth2Token;
        self.imapSession.authType = MCOAuthTypeXOAuth2;
    }
	self.imapSession.connectionType = MCOConnectionTypeTLS;
    MasterViewController * __weak weakSelf = self;
	self.imapSession.connectionLogger = ^(void * connectionID, MCOConnectionLogType type, NSData * data) {
        @synchronized(weakSelf) {
            if (type != MCOConnectionLogTypeSentPrivate) {
//                NSLog(@"event logged:%p %i withData: %@", connectionID, type, [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding]);
            }
        }
    };
	
	// Reset the inbox
	self.messages = nil;
	self.totalNumberOfInboxMessages = -1;
	self.isLoading = NO;
	self.messagePreviews = [NSMutableDictionary dictionary];
	[self.tableView reloadData];
    
	NSLog(@"checking account");
	self.imapCheckOp = [self.imapSession checkAccountOperation];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    });
    
	[self.imapCheckOp start:^(NSError *error) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
        });
        
		MasterViewController *strongSelf = weakSelf;
		NSLog(@"finished checking account.");
		if (error == nil) {
			[strongSelf loadLastNMessages:NUMBER_OF_MESSAGES_TO_LOAD];
		} else {
			NSLog(@"error loading account: %@", error);
		}
		
		strongSelf.imapCheckOp = nil;
	}];
}

- (void)loadLastNMessages:(NSUInteger)nMessages
{
	self.isLoading = YES;
	
	MCOIMAPMessagesRequestKind requestKind = (MCOIMAPMessagesRequestKind)
	(MCOIMAPMessagesRequestKindHeaders | MCOIMAPMessagesRequestKindStructure |
	 MCOIMAPMessagesRequestKindInternalDate | MCOIMAPMessagesRequestKindHeaderSubject |
	 MCOIMAPMessagesRequestKindFlags);
	
	NSString *inboxFolder = @"INBOX";
	MCOIMAPFolderInfoOperation *inboxFolderInfo = [self.imapSession folderInfoOperation:inboxFolder];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    });
    
	[inboxFolderInfo start:^(NSError *error, MCOIMAPFolderInfo *info)
	{
        
		BOOL totalNumberOfMessagesDidChange =
		self.totalNumberOfInboxMessages != [info messageCount];
		
		self.totalNumberOfInboxMessages = [info messageCount];
		
		NSUInteger numberOfMessagesToLoad =
		MIN(self.totalNumberOfInboxMessages, nMessages);
		
		if (numberOfMessagesToLoad == 0)
		{
			self.isLoading = NO;
			return;
		}
		
		MCORange fetchRange;
		
		// If total number of messages did not change since last fetch,
		// assume nothing was deleted since our last fetch and just
		// fetch what we don't have
        
        if (belowMessageNumber - 10 <= 0 && belowMessageNumber == 1){
            dispatch_async(dispatch_get_main_queue(), ^{
                [MBProgressHUD hideAllHUDsForView:self.view animated:YES];

                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"No More Messages to show" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                [alertView show];
            });
            return;
        }
        
        if (belowMessageNumber == 0){
            if (self.totalNumberOfInboxMessages - 10 <= 0){
                belowMessageNumber = 1;
            }else{
                belowMessageNumber = self.totalNumberOfInboxMessages - 10;
            }
        }else{
            if (belowMessageNumber - 10 <= 0){
                belowMessageNumber = 1;
            }else{
                belowMessageNumber = belowMessageNumber - 10;
            }
        }

        
		if (!totalNumberOfMessagesDidChange && self.messages.count)
		{
			numberOfMessagesToLoad -= self.messages.count;
            
			fetchRange =
			MCORangeMake(belowMessageNumber,
						 (10));
		}else
		{
			fetchRange =
			MCORangeMake(belowMessageNumber,
						 10);
		}
		
        
		self.imapMessagesFetchOp =
		[self.imapSession fetchMessagesByNumberOperationWithFolder:inboxFolder
													   requestKind:requestKind
														   numbers:
         [MCOIndexSet indexSetWithRange:fetchRange]];
		
		[self.imapMessagesFetchOp setProgress:^(unsigned int progress) {
//			NSLog(@"Progress: %u of %u", progress, numberOfMessagesToLoad);
		}];

        

		__weak MasterViewController *weakSelf = self;
		[self.imapMessagesFetchOp start:
		 ^(NSError *error, NSArray *messages, MCOIndexSet *vanishedMessages)
		{
            dispatch_async(dispatch_get_main_queue(), ^{
                [MBProgressHUD hideHUDForView:self.view animated:YES];
            });

            NSMutableArray *filteredMessageArray = [[NSMutableArray alloc] init];
            
            for (MCOIMAPMessage *msg in messages) {
                NSLog(@"Description : %@", msg.description);
//                if ([self checkIfRelevantOrNot:msg emailAddress:@"wajeeha.kamran@netsoltech.com"]){
//                    [filteredMessageArray addObject:msg];
//                }
                
                if ([self checkIfRelevantOrNot:msg emailAddress:@"aaban.murtaza@netsoltech.com"]){
                    [filteredMessageArray addObject:msg];
                }

            }
            
			MasterViewController *strongSelf = weakSelf;
			NSLog(@"fetched all messages.");
			
			self.isLoading = NO;
			
			NSSortDescriptor *sort =
			[NSSortDescriptor sortDescriptorWithKey:@"header.date" ascending:NO];
			
            NSMutableArray *combinedMessages = [[NSMutableArray alloc] init];
//			[NSMutableArray arrayWithArray:filteredMessageArray];
            
            [combinedMessages addObjectsFromArray:filteredMessageArray];
			[combinedMessages addObjectsFromArray:strongSelf.messages];
			
            
			strongSelf.messages =
			[combinedMessages sortedArrayUsingDescriptors:@[sort]];
            [self fetchReadAndFavMessageUIDs];
//			[strongSelf.tableView reloadData];
		}];
	}];
}

- (void)searchBar:(UISearchBar *)searchBar textDidChange:(NSString *)searchText{
    
    NSSortDescriptor *sort =
    [NSSortDescriptor sortDescriptorWithKey:@"header.date" ascending:NO];

    
    if([searchBar.text isEqualToString:@""] || searchBar.text==nil) {
        searchBool = NO;
        [self.filteredEmails removeAllObjects];
        [self.tableView reloadData];
        [searchBar resignFirstResponder];
    }else{
        searchBool = YES;
        [self.filteredEmails removeAllObjects];
        
        for (MCOIMAPMessage * msg in self.messages) {
            if ([[msg.header.subject lowercaseString] containsString:[searchBar.text lowercaseString]]){
                [self.filteredEmails addObject:msg];
            }
        }
        
        NSArray *temp = [self.filteredEmails sortedArrayUsingDescriptors:@[sort]];
        self.filteredEmails = [NSMutableArray arrayWithArray:temp];
        [self.tableView reloadData];
    }
    
    NSLog(@"asdasd");
}

//-(void)searchBarTextDidEndEditing:(UISearchBar *)searchBar
//{
//    [searchBar resignFirstResponder];
//}

//-(BOOL) searchBar:(UISearchBar *)searchBar shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
//    // This method has been called when u enter some text on search or Cancel the search.
//    if([searchBar.text isEqualToString:@""] || searchBar.text==nil) {
//        // Nothing to search, empty result.
//        searchBool = NO;
//        [self.filteredEmails removeAllObjects];
//        [self.tableView reloadData];
//    }else{
//        searchBool = YES;
//        [self.filteredEmails removeAllObjects];
//        
//        for (MCOIMAPMessage * msg in self.messages) {
//            if ([msg.header.subject containsString:searchBar.text]){
//                [self.filteredEmails addObject:msg];
//            }
//        }
//        
//        [self.tableView reloadData];
//    }
//    
//    return YES;
//}


-(BOOL) checkIfRelevantOrNot :(MCOIMAPMessage *)msg emailAddress :(NSString *)email{

    NSArray *chunks = [msg.description componentsSeparatedByString: @"From: mailcore::Address:"];
    NSArray *fromPart = [chunks[1] componentsSeparatedByString:@"To: [mailcore::Address:"];

    if ([fromPart.firstObject containsString:email]){
        return YES;
    }

    if ([[fromPart[1] componentsSeparatedByString:@"Reply-To: [mailcore::Address:"].firstObject containsString:email]){
        return YES;
    }
    
    return NO;
}

- (void)didReceiveMemoryWarning {
	[super didReceiveMemoryWarning];
	NSLog(@"%s",__PRETTY_FUNCTION__);
}

#pragma mark - Table View

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    if (searchBool || readBool || unreadBool || favBool){
        return 1;
    }else{
        return 2;
    }
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    if (searchBool){
        return self.filteredEmails.count;
    }else if (readBool){
        return self.readEmails.count;
    }else if (unreadBool){
        return self.unreadEmails.count;
    }else if (favBool){
        return self.favEmails.count;
    }else{
        if (section == 1)
        {
            if (self.totalNumberOfInboxMessages >= 0)
                return 1;
            
            return 0;
        }
        return self.messages.count;
    }
}

-(void)didSelectFromDropDown:(NSIndexPath *)indexPath{

    favBool = NO;
    [self.favBtn setImage:[UIImage imageNamed:@"favMarkUnselected"] forState:UIControlStateNormal];

    
    if (indexPath.row == 0){
        allBool = YES;
        readBool = NO;
        unreadBool = NO;
        searchBool = NO;
    }else if (indexPath.row == 1){
        readBool = YES;
        unreadBool = NO;
        allBool = NO;
        searchBool = NO;
    }else{
        unreadBool = YES;
        readBool = NO;
        allBool = NO;
        searchBool = NO;
   }
    [self.dropDown hideDropDown];
    [self.tableView reloadData];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
	switch (indexPath.section)
	{
		case 0:
		{
			MCTTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:mailCellIdentifier forIndexPath:indexPath];
            MCOIMAPMessage *message;
            
            if (searchBool){
                message = self.filteredEmails[indexPath.row];
            }else if (readBool){
                message = self.readEmails[indexPath.row];
            }else if (unreadBool){
                message = self.unreadEmails[indexPath.row];
            }else if (favBool){
                message = self.favEmails[indexPath.row];
            }else{
                message = self.messages[indexPath.row];
            }
			
            if ([message.isFav boolValue]){
                [cell.favBtn setImage:[UIImage imageNamed:@"newsFavIconSelected"] forState:UIControlStateNormal];
            }else{
                [cell.favBtn setImage:[UIImage imageNamed:@"newsFavIconUnselected"] forState:UIControlStateNormal];
            }
            
            if (![message.isRead boolValue] || unreadBool){
                [cell makeCellUnread];
            }else{
                [cell makeCellRead];
            }
            
            cell.message = message;
            cell.delegate = self;
            cell.indexPath = indexPath;
            cell.cirleLbl.layer.cornerRadius = 25.0;
            [cell.cirleLbl.layer setMasksToBounds:YES];
            [cell.cirleLbl setText:[message.header.subject substringToIndex:1]];
            [cell.cirleLbl setBackgroundColor:[self colorFromSubject:[message.header.subject substringToIndex:1]]];
            [cell.bigSubjectLbl setText:message.header.subject];
            cell.detailLbl.text = @"";
			NSString *uidKey = [NSString stringWithFormat:@"%d", message.uid];
			NSString *cachedPreview = self.messagePreviews[uidKey];
			
			if (cachedPreview){
                [cell.detailLbl setText:cachedPreview];
			}
			else{
				cell.messageRenderingOperation = [self.imapSession plainTextBodyRenderingOperationWithMessage:message
																									   folder:@"INBOX"];
                [cell.activityIndicator startAnimating];
				[cell.messageRenderingOperation start:^(NSString * plainTextBodyString, NSError * error) {
                    [cell.activityIndicator stopAnimating];
					cell.detailLbl.text = plainTextBodyString;
					cell.messageRenderingOperation = nil;
					self.messagePreviews[uidKey] = plainTextBodyString;
				}];
			}
			[cell setBackgroundColor:[UIColor clearColor]];
			return cell;
			break;
		}
			
		case 1:
		{
			LoadMoreCell *cell =
			[tableView dequeueReusableCellWithIdentifier:@"LoadMoreCell"];
			
//			if (!cell)
//			{
//				cell =
//				[[LoadMoreCell alloc] initWithStyle:UITableViewCellStyleSubtitle
//									   reuseIdentifier:@"LoadMoreCell"];
//				
//				cell.loadMoreLabel.font = [UIFont boldSystemFontOfSize:12];
//				cell.textLabel.textAlignment = NSTextAlignmentCenter;
//				cell.detailTextLabel.textAlignment = NSTextAlignmentCenter;
//			}
			
			cell.loadMoreLabel.text =
			[NSString stringWithFormat:@"Load More message(s)"];
			
//			cell.accessoryView = self.loadMoreActivityView;
			
			if (self.isLoading)
				[cell.activity startAnimating];
			else
				[cell.activity stopAnimating];
			
            [cell setBackgroundColor:[UIColor clearColor]];
            
			return cell;
			break;
		}
			
		default:
			return nil;
			break;
	}
}

-(UIColor *) colorFromSubject :(NSString *) startingAlphabet{
    UIColor *color;
    
    if ([[startingAlphabet lowercaseString] isEqualToString:@"a"]){
        color = [UIColor colorWithRed:100.0/255.0 green:165.0/255.0 blue:187.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"b"]){
        color = [UIColor colorWithRed:105.0/255.0 green:132.0/255.0 blue:51.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"c"]){
        color = [UIColor colorWithRed:204.0/255.0 green:81.0/255.0 blue:101.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"d"]){
        color = [UIColor colorWithRed:255.0/255.0 green:130.0/255.0 blue:2.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"e"]){
        color = [UIColor colorWithRed:218.0/255.0 green:16.0/255.0 blue:92/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"f"]){
        color = [UIColor colorWithRed:97.0/255.0 green:170.0/255.0 blue:177.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"g"]){
        color = [UIColor colorWithRed:163.0/255.0 green:28.0/255.0 blue:32.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"h"]){
        color = [UIColor colorWithRed:163.0/255.0 green:29.0/255.0 blue:144.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"i"]){
        color = [UIColor colorWithRed:185.0/255.0 green:29.0/255.0 blue:144.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"j"]){
        color = [UIColor colorWithRed:201.0/255.0 green:29.0/255.0 blue:112.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"k"]){
        color = [UIColor colorWithRed:200.0/255.0 green:44.0/255.0 blue:58.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"l"]){
        color = [UIColor colorWithRed:1.0/255.0 green:128.0/255.0 blue:181.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"m"]){
        color = [UIColor colorWithRed:234.0/255.0 green:62.0/255.0 blue:112.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"n"]){
        color = [UIColor colorWithRed:75.0/255.0 green:196.0/255.0 blue:213.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"o"]){
        color = [UIColor colorWithRed:148.0/255.0 green:113.0/255.0 blue:219.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"p"]){
        color = [UIColor colorWithRed:113.0/255.0 green:184.0/255.0 blue:77.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"q"]){
        color = [UIColor colorWithRed:228.0/255.0 green:97.0/255.0 blue:97.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"r"]){
        color = [UIColor colorWithRed:180.0/255.0 green:104.0/255.0 blue:116.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"s"]){
        color = [UIColor colorWithRed:219.0/255.0 green:169.0/255.0 blue:98.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"t"]){
        color = [UIColor colorWithRed:231.0/255.0 green:57.0/255.0 blue:110.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"u"]){
        color = [UIColor colorWithRed:74.0/255.0 green:142.0/255.0 blue:129.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"v"]){
        color = [UIColor colorWithRed:218.0/255.0 green:17.0/255.0 blue:171.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"w"]){
        color = [UIColor colorWithRed:17.0/255.0 green:218.0/255.0 blue:17.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"x"]){
        color = [UIColor colorWithRed:153.0/255.0 green:173.0/255.0 blue:153.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"y"]){
        color = [UIColor colorWithRed:51.0/255.0 green:177.0/255.0 blue:51.0/255.0 alpha:1];
    }else if ([[startingAlphabet lowercaseString] isEqualToString:@"z"]){
        color = [UIColor colorWithRed:17.0/255.0 green:177.0/255.0 blue:153.0/255.0 alpha:1];
    }
    return color;
}

- (IBAction)FavBtnPressed:(id)sender {
    favBool = !favBool;
    readBool = NO;
    unreadBool = NO;
    searchBool = NO;
    allBool = YES;
    
    if (favBool){
        [self.favBtn setImage:[UIImage imageNamed:@"favMarkSelected"] forState:UIControlStateNormal];
        if (self.favEmails.count == 0){
            [self.view makeToast:@"No Favourites Found"];
        }
    }else{
        [self.favBtn setImage:[UIImage imageNamed:@"favMarkUnselected"] forState:UIControlStateNormal];
    }
    [self.tableView reloadData];
}

- (void)showSettingsViewController:(id)sender {
	[self.imapMessagesFetchOp cancel];
	
	SettingsViewController *settingsViewController = [[SettingsViewController alloc] initWithNibName:nil bundle:nil];
	settingsViewController.delegate = self;
    UINavigationController *nav = [[UINavigationController alloc] initWithRootViewController:settingsViewController];
	[self presentViewController:nav animated:YES completion:nil];
}

- (void)settingsViewControllerFinished:(SettingsViewController *)viewController {
//	[self dismissViewControllerAnimated:YES completion:nil];
	
//	NSString *username = [[NSUserDefaults standardUserDefaults] stringForKey:UsernameKey];
//	NSString *password = [[FXKeychain defaultKeychain] objectForKey:PasswordKey];
//	NSString *hostname = [[NSUserDefaults standardUserDefaults] objectForKey:HostnameKey];
//    
//	if (![username isEqualToString:self.imapSession.username] ||
//		![password isEqualToString:self.imapSession.password] ||
//		![hostname isEqualToString:self.imapSession.hostname]) {
//		self.imapSession = nil;
//		[self loadAccountWithUsername:username password:password hostname:hostname oauth2Token:nil];
//	}
}

-(void)reloadCellAtIndexPath:(NSIndexPath *)indexPath{
//    [self.tableView reloadRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:UITableViewRowAnimationNone];
    [self.tableView reloadData];
}


- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
	switch (indexPath.section)
	{
		case 0:
		{
            MCOIMAPMessage *msg;
            
            if (searchBool){
                msg = self.filteredEmails[indexPath.row];
            }else if (readBool){
                msg = self.readEmails[indexPath.row];
            }else if (unreadBool){
                msg = self.unreadEmails[indexPath.row];
            }else{
                msg = self.messages[indexPath.row];
            }
            
            DetailPageViewController *vc = [[UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil] instantiateViewControllerWithIdentifier:@"detailPageViewController"];

            vc.folder = @"INBOX";
            vc.message = msg;
            vc.session = self.imapSession;
            vc.emailListVCRef = self;
            vc.indexPath = indexPath;
            vc.messageBody = msg.description;
            vc.messageHeader = msg.header.subject;
            
			[self.navigationController pushViewController:vc animated:YES];
			
			break;
		}
			
		case 1:
		{
			UITableViewCell *cell = [tableView cellForRowAtIndexPath:indexPath];
			
			if (!self.isLoading &&
				self.messages.count < self.totalNumberOfInboxMessages)
			{
				[self loadLastNMessages:self.messages.count + NUMBER_OF_MESSAGES_TO_LOAD];
			}
			
			[tableView deselectRowAtIndexPath:indexPath animated:YES];
			break;
		}
			
		default:
			break;
	}

    
    [self.tableView deselectRowAtIndexPath:indexPath animated:YES];
    
}

@end
