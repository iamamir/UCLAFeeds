//
//  SettingsViewController.m
//  iOS UI Test
//
//  Created by Aaban Tariq on 4/8/13.
//  Copyright (c) 2013 AppJon. All rights reserved.
//

#import "SettingsViewController.h"

#import "FXKeychain.h"


@implementation SettingsViewController

- (IBAction)registerBtnPressed:(id)sender {
    
    [[NSUserDefaults standardUserDefaults] setObject:self.registerEmail.text ?: @"" forKey:UsernameKey];
    [[NSUserDefaults standardUserDefaults] setObject:HostnameValue ?: @"" forKey:HostnameKey];
//    MasterViewController *viewController = [[UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil] instantiateViewControllerWithIdentifier:@"masterViewController"];
//    [self.navigationController pushViewController:viewController animated:YES];
//    self.delegate = viewController;
//    [self.delegate settingsViewControllerFinished:self];
    
    [self registerWerbService];
}
- (IBAction)segmentPressed:(id)sender {
    if (loginBool){
        loginBool = NO;
        [self.registerView setHidden:NO];
        [self.loginView setHidden:YES];
    }else{
        loginBool = YES;
        [self.registerView setHidden:YES];
        [self.loginView setHidden:NO];
    }
}

-(void) registerWerbService{
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    config.HTTPAdditionalHeaders = @{ @"Content-Type"  : @"application/json"};
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config delegate:nil delegateQueue:nil];
    
    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URLStringForRegisterUserService]];
    request.HTTPMethod = @"POST";
    
    NSMutableDictionary *dictionary = [[NSMutableDictionary alloc] init];
    
    [dictionary setObject:[[NSUserDefaults standardUserDefaults] objectForKey:UsernameKey] forKey:webserviceUsernameKey];
    [dictionary setObject:@"consumer" forKey:@"type"];
    
    NSData *jsonDict=[NSJSONSerialization dataWithJSONObject:dictionary options:0 error:nil];
    [request setHTTPBody:jsonDict];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    });
    [[session uploadTaskWithRequest:request fromData:jsonDict completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        if (error){
            dispatch_async(dispatch_get_main_queue(), ^{
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"User Register Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                [alertView show];
            });
            
        }else{
            
            dispatch_async(dispatch_get_main_queue(), ^{
                [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
            });
            
            NSDictionary* json = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
            NSLog(@"Data %@", json);
            
            if ([[json objectForKey:@"statusCode"] integerValue] == 200){
                
                dispatch_async(dispatch_get_main_queue(), ^{
                    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Success" message:@"You have been registered, Please check your email" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                    [alertView show];
                });

            }else{
                dispatch_async(dispatch_get_main_queue(), ^{
                    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"User Register Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                    [alertView show];
                });
            }
        }
    }] resume];
}


-(BOOL) textFieldShouldReturn:(UITextField *)textField{
    
    [textField resignFirstResponder];
    return YES;
}

-(void) loginWebService{
    
    NSURLSessionConfiguration *config = [NSURLSessionConfiguration defaultSessionConfiguration];
    config.HTTPAdditionalHeaders = @{ @"Content-Type"  : @"application/json"};
    
    NSURLSession *session = [NSURLSession sessionWithConfiguration:config delegate:nil delegateQueue:nil];

    NSMutableURLRequest *request = [[NSMutableURLRequest alloc] initWithURL:[NSURL URLWithString:URLStringForLoginService]];
    request.HTTPMethod = @"POST";

    NSMutableDictionary *dictionary = [[NSMutableDictionary alloc] init];
    
    [dictionary setObject:[[NSUserDefaults standardUserDefaults] objectForKey:UsernameKey] forKey:webserviceUsernameKey];
    [dictionary setObject:[[FXKeychain defaultKeychain] objectForKey:PasswordKey] forKey:webservicePasswordKey];
    [dictionary setObject:@"consumer" forKey:@"type"];
    
    NSData *jsonDict=[NSJSONSerialization dataWithJSONObject:dictionary options:0 error:nil];
    [request setHTTPBody:jsonDict];
    
    dispatch_async(dispatch_get_main_queue(), ^{
        [MBProgressHUD showHUDAddedTo:self.view animated:YES];
    });
    
    [[session dataTaskWithRequest:request completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
        dispatch_async(dispatch_get_main_queue(), ^{
            [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
        });
        if (error != nil){
            dispatch_async(dispatch_get_main_queue(), ^{
                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Login Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                [alertView show];
            });
        }else{
            
            NSString *string = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
            NSLog(@"Data %@", string);
            
            NSDictionary* json = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
            NSLog(@"Data %@", json);
            
            if ([[json objectForKey:@"statusCode"] integerValue] == 200){
                [self moveToEmailListViewController];
            }else{
                dispatch_async(dispatch_get_main_queue(), ^{
                    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Login Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
                    [alertView show];
                });
            }
        }

    }] resume];
    
//    [[session uploadTaskWithRequest:request fromData:jsonDict completionHandler:^(NSData *data, NSURLResponse *response, NSError *error) {
//        
//        dispatch_async(dispatch_get_main_queue(), ^{
//            [MBProgressHUD hideAllHUDsForView:self.view animated:YES];
//        });
//        if (error != nil){
//            dispatch_async(dispatch_get_main_queue(), ^{
//                UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Login Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
//                [alertView show];
//            });
//        }else{
//            
//            NSString *string = [[NSString alloc] initWithData:data encoding:NSUTF8StringEncoding];
//            NSLog(@"Data %@", string);
//            
//            NSDictionary* json = [NSJSONSerialization JSONObjectWithData:data options:kNilOptions error:&error];
//            NSLog(@"Data %@", json);
//
//            if ([[json objectForKey:@"statusCode"] integerValue] == 200){
//                [self moveToEmailListViewController];
//            }else{
//                dispatch_async(dispatch_get_main_queue(), ^{
//                    UIAlertView *alertView = [[UIAlertView alloc] initWithTitle:@"Error" message:@"Login Error" delegate:nil cancelButtonTitle:@"Ok" otherButtonTitles:nil];
//                    [alertView show];
//                });
//            }
//        }
//    }] resume];
}

-(MasterViewController *)masterViewController{
    if (!_masterViewController){
        _masterViewController = [[UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil] instantiateViewControllerWithIdentifier:@"masterViewController"];
    }
    return _masterViewController;
}

-(void)destroyMaster{
    _masterViewController = nil;
}
-(void) moveToEmailListViewController {
    dispatch_async(dispatch_get_main_queue(), ^{
        self.masterViewController.delegate = self;
        [self.navigationController pushViewController:self.masterViewController animated:YES];
//        self.delegate = self.masterViewController;
//        [self.delegate settingsViewControllerFinished:self];
    });
}

-(void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:YES];
    [self.loginEmail setText:@""];
    [self.passwordTxtFld setText:@""];
    [self.registerEmail setText:@""];
//    [self onTimer];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = @"Login";
    
    
    self.view.backgroundColor = [UIColor lightGrayColor];

    self.passwordTxtFld.text = [[NSUserDefaults standardUserDefaults] stringForKey:UsernameKey];
    [self.passwordTxtFld setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];
    
    self.loginEmail.text = [[NSUserDefaults standardUserDefaults] stringForKey:UsernameKey];
    [self.loginEmail setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];

    self.registerEmail.text = [[NSUserDefaults standardUserDefaults] stringForKey:UsernameKey];
    [self.registerEmail setValue:[UIColor lightGrayColor] forKeyPath:@"_placeholderLabel.textColor"];

    self.hostnameTextField.text = HostnameValue;
    
//    [NSTimer scheduledTimerWithTimeInterval:60.0f target:self selector:@selector(onTimer) userInfo:nil repeats:YES];
    
}

- (void) onTimer {
    [UIView beginAnimations: @"scrollAnimation" context:nil];
    [UIView setAnimationDuration: 60.0f];
    
    if (self.contentOffset == 1600){
        self.contentOffset = 0;
    }else{
        self.contentOffset = 1600;
    }
    
    [self.scrollView setContentOffset:CGPointMake(self.contentOffset,0)];
    
    [UIView commitAnimations];
}

- (IBAction)loginBtnPressed:(id)sender {
    [[NSUserDefaults standardUserDefaults] setObject:self.loginEmail.text ?: @"" forKey:UsernameKey];
    [[FXKeychain defaultKeychain] setObject:self.passwordTxtFld.text ?: @"" forKey:PasswordKey];
    [[NSUserDefaults standardUserDefaults] setObject:HostnameValue ?: @"" forKey:HostnameKey];
    
    
//    MasterViewController *viewController = [[UIStoryboard storyboardWithName:@"MainStoryboard" bundle:nil] instantiateViewControllerWithIdentifier:@"masterViewController"];
//    [self.navigationController pushViewController:viewController animated:YES];
//    self.delegate = viewController;
//    [self.delegate settingsViewControllerFinished:self];
    [self loginWebService];
}
@end
