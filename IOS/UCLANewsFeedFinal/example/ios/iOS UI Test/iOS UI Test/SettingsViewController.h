//
//  SettingsViewController.h
//  iOS UI Test
//
//  Created by Aaban Tariq on 4/8/13.
//  Copyright (c) 2013 AppJon. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "MBProgressHUD.h"

#define UsernameKey @"username"
#define webserviceUsernameKey @"email"
#define webservicePasswordKey @"password"
#define URLStringForRegisterUserService @"http://10.14.9.4:9000/RegisterUser"
#define URLStringForLoginService @"http://10.14.9.4:9000/LoginUser"
#define PasswordKey @"password"
#define HostnameKey @"hostname"
#define FetchFullMessageKey @"FetchFullMessageEnabled"
#define OAuthEnabledKey @"OAuth2Enabled"
#define HostnameValue @"imap.gmail.com"



#import "MasterViewController.h"

//extern NSString * const FetchFullMessageKey;
//extern NSString * const OAuthEnabledKey;

@protocol SettingsViewControllerDelegate;

@interface SettingsViewController : UIViewController<destroyInstanceDelegate>{
    BOOL loginBool;
}

@property (weak, nonatomic) IBOutlet UITextField *registerEmail;
@property (weak, nonatomic) IBOutlet UITextField *loginEmail;
@property (weak, nonatomic) IBOutlet UITextField *passwordTxtFld;
@property (weak, nonatomic) IBOutlet UIView *registerView;
@property (weak, nonatomic) IBOutlet UIView *loginView;

@property (weak, nonatomic) IBOutlet UITextField *hostnameTextField;

@property (weak, nonatomic) MasterViewController * masterViewController;
@property (weak, nonatomic) IBOutlet UIScrollView *scrollView;
@property (nonatomic) int contentOffset;

//@property (weak, nonatomic) IBOutlet UISwitch *fetchFullMessageSwitch;
//@property (weak, nonatomic) IBOutlet UISwitch *useOAuth2Switch;

@property (nonatomic, weak) id<SettingsViewControllerDelegate> delegate;
- (IBAction)done:(id)sender;

- (IBAction)registerBtnPressed:(id)sender;
- (IBAction)loginBtnPressed:(id)sender;

@end

@protocol SettingsViewControllerDelegate <NSObject>
- (void)settingsViewControllerFinished:(SettingsViewController *)viewController;
@end