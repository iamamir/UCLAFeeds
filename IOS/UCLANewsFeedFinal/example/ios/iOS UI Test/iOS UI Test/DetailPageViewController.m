//
//  DetailPageViewController.m
//  iOS UI Test
//
//  Created by Aaban Tariq Murtaza on 19/05/2015.
//  Copyright (c) 2015 AppJon. All rights reserved.
//

#import "DetailPageViewController.h"

@interface DetailPageViewController ()

@end

@implementation DetailPageViewController

@synthesize folder = _folder;
@synthesize session = _session;


- (void) awakeFromNib
{
    _storage = [[NSMutableDictionary alloc] init];
    _ops = [[NSMutableArray alloc] init];
    _pending = [[NSMutableSet alloc] init];
    _callbacks = [[NSMutableDictionary alloc] init];
    self.messagePreviews = [NSMutableDictionary dictionary];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.cirlceLabel.layer.cornerRadius = 25.0;
    [self.cirlceLabel.layer setMasksToBounds:YES];

    [self.headerLabel setText:self.messageHeader];
    
    NSDateFormatter *format = [[NSDateFormatter alloc] init];
    [format setDateFormat:@"EEE, dd, MMM yyyy, hh:mm:ss"];
    [self.dateLabel setText:[format stringFromDate:self.message.header.date]];
    
    NSString *uidKey = [NSString stringWithFormat:@"%d", self.message.uid];
    NSString *cachedPreview = self.messagePreviews[uidKey];
    
    if (cachedPreview){
        [self.bodyTextView setText:cachedPreview];
    }
    else{
        self.messageRenderingOperation = [self.session plainTextBodyRenderingOperationWithMessage:self.message
                                                                                               folder:@"INBOX"];
        
        [self.activityIndicator startAnimating];
        
        [self.messageRenderingOperation start:^(NSString * plainTextBodyString, NSError * error) {
            
            [self.activityIndicator stopAnimating];
            
            self.bodyTextView.text = plainTextBodyString;
            self.messageRenderingOperation = nil;
            self.messagePreviews[uidKey] = plainTextBodyString;
        }];
    }
    
    [self.cirlceLabel setText:[self.message.header.subject substringToIndex:1]];
    [self.cirlceLabel setBackgroundColor:[self colorFromSubject:[self.message.header.subject substringToIndex:1]]];
    
    if ([self.message.isFav boolValue]){
        [self.favBtn setImage:[UIImage imageNamed:@"newsFavIconSelected"] forState:UIControlStateNormal];
    }else{
        [self.favBtn setImage:[UIImage imageNamed:@"newsFavIconUnselected"] forState:UIControlStateNormal];
    }
    
    self.message.isRead = [NSNumber numberWithBool:YES];
    [self.emailListVCRef addToReadListWebService :self.message];
    [self.emailListVCRef reloadCellAtIndexPath:self.indexPath];
}
- (IBAction)popView:(id)sender {
    [self.navigationController popViewControllerAnimated:NO];
}
- (IBAction)logout:(id)sender {
    [self.navigationController popToRootViewControllerAnimated:YES];
}


- (void) setMessage:(MCOIMAPMessage *)message
{
    MCLog("set message : %s", message.description.UTF8String);
    for(MCOOperation * op in _ops) {
        [op cancel];
    }
    [_ops removeAllObjects];
    
    [_callbacks removeAllObjects];
    [_pending removeAllObjects];
    [_storage removeAllObjects];
    _message = message;
}

- (MCOIMAPMessage *) message
{
    return _message;
}

- (MCOIMAPFetchContentOperation *) _fetchIMAPPartWithUniqueID:(NSString *)partUniqueID folder:(NSString *)folder
{
    MCLog("%s is missing, fetching", partUniqueID.description.UTF8String);
    
    if ([_pending containsObject:partUniqueID]) {
        return nil;
    }
    
    MCOIMAPPart * part = (MCOIMAPPart *) [_message partForUniqueID:partUniqueID];
    NSAssert(part != nil, @"part != nil");
    
    [_pending addObject:partUniqueID];
    
    MCOIMAPFetchContentOperation * op = [_session fetchMessageAttachmentOperationWithFolder:folder uid:[_message uid] partID:[part partID] encoding:[part encoding]];
    [_ops addObject:op];
    [op start:^(NSError * error, NSData * data) {
        if ([error code] != MCOErrorNone) {
            [self _callbackForPartUniqueID:partUniqueID error:error];
            return;
        }
        
        NSAssert(data != NULL, @"data != nil");
        [_ops removeObject:op];
        [_storage setObject:data forKey:partUniqueID];
        [_pending removeObject:partUniqueID];
        MCLog("downloaded %s", partUniqueID.description.UTF8String);
        
        [self _callbackForPartUniqueID:partUniqueID error:nil];
    }];
    
    return op;
}

typedef void (^DownloadCallback)(NSError * error);

- (void) _callbackForPartUniqueID:(NSString *)partUniqueID error:(NSError *)error
{
    NSArray * blocks;
    blocks = [_callbacks objectForKey:partUniqueID];
    for(DownloadCallback block in blocks) {
        block(error);
    }
}

- (NSString *) MCOMessageView_templateForAttachment:(MCOMessageView *)view
{
    return @"<div><img src=\"http://www.iconshock.com/img_jpg/OFFICE/general/jpg/128/attachment_icon.jpg\"/></div>\
    {{#HASSIZE}}\
    <div>- {{FILENAME}}, {{SIZE}}</div>\
    {{/HASSIZE}}\
    {{#NOSIZE}}\
    <div>- {{FILENAME}}</div>\
    {{/NOSIZE}}";
}

- (NSString *) MCOMessageView_templateForMessage:(MCOMessageView *)view
{
    return @"<div style=\"padding-bottom: 20px; font-family: Helvetica; font-size: 13px;\">{{HEADER}}</div><div>{{BODY}}</div>";
}

- (BOOL) MCOMessageView:(MCOMessageView *)view canPreviewPart:(MCOAbstractPart *)part
{
    // tiff, tif, pdf
    NSString * mimeType = [[part mimeType] lowercaseString];
    if ([mimeType isEqualToString:@"image/tiff"]) {
        return YES;
    }
    else if ([mimeType isEqualToString:@"image/tif"]) {
        return YES;
    }
    else if ([mimeType isEqualToString:@"application/pdf"]) {
        return YES;
    }
    
    NSString * ext = nil;
    if ([part filename] != nil) {
        if ([[part filename] pathExtension] != nil) {
            ext = [[[part filename] pathExtension] lowercaseString];
        }
    }
    if (ext != nil) {
        if ([ext isEqualToString:@"tiff"]) {
            return YES;
        }
        else if ([ext isEqualToString:@"tif"]) {
            return YES;
        }
        else if ([ext isEqualToString:@"pdf"]) {
            return YES;
        }
    }
    
    return NO;
}

- (NSString *) MCOMessageView:(MCOMessageView *)view filteredHTML:(NSString *)html
{
    return html;
}

- (NSData *) MCOMessageView:(MCOMessageView *)view dataForPartWithUniqueID:(NSString *)partUniqueID
{
    if ([[NSUserDefaults standardUserDefaults] boolForKey:@"FetchFullMessageEnabled"]) {
        MCOAttachment * attachment = (MCOAttachment *) [[_messageView message] partForUniqueID:partUniqueID];
        return [attachment data];
    }
    else {
        NSData * data = [_storage objectForKey:partUniqueID];
        return data;
    }
}

- (void) MCOMessageView:(MCOMessageView *)view fetchDataForPartWithUniqueID:(NSString *)partUniqueID
     downloadedFinished:(void (^)(NSError * error))downloadFinished
{
    MCOIMAPFetchContentOperation * op = [self _fetchIMAPPartWithUniqueID:partUniqueID folder:_folder];
    [op setProgress:^(unsigned int current, unsigned int maximum) {
        MCLog("progress content: %u/%u", current, maximum);
    }];
    if (op != nil) {
        [_ops addObject:op];
    }
    if (downloadFinished != NULL) {
        NSMutableArray * blocks;
        blocks = [_callbacks objectForKey:partUniqueID];
        if (blocks == nil) {
            blocks = [NSMutableArray array];
            [_callbacks setObject:blocks forKey:partUniqueID];
        }
        [blocks addObject:[downloadFinished copy]];
    }
}

- (NSData *) MCOMessageView:(MCOMessageView *)view previewForData:(NSData *)data isHTMLInlineImage:(BOOL)isHTMLInlineImage
{
    if (isHTMLInlineImage) {
        return data;
    }
    else {
        return [self _convertToJPEGData:data];
    }
}

#define IMAGE_PREVIEW_HEIGHT 300
#define IMAGE_PREVIEW_WIDTH 500

- (NSData *) _convertToJPEGData:(NSData *)data {
    CGImageSourceRef imageSource;
    CGImageRef thumbnail;
    NSMutableDictionary * info;
    int width;
    int height;
    float quality;
    
    width = IMAGE_PREVIEW_WIDTH;
    height = IMAGE_PREVIEW_HEIGHT;
    quality = 1.0;
    
    imageSource = CGImageSourceCreateWithData((__bridge CFDataRef) data, NULL);
    if (imageSource == NULL)
        return nil;
    
    info = [[NSMutableDictionary alloc] init];
    [info setObject:(id) kCFBooleanTrue forKey:(__bridge id) kCGImageSourceCreateThumbnailWithTransform];
    [info setObject:(id) kCFBooleanTrue forKey:(__bridge id) kCGImageSourceCreateThumbnailFromImageAlways];
    [info setObject:(id) [NSNumber numberWithFloat:(float) IMAGE_PREVIEW_WIDTH] forKey:(__bridge id) kCGImageSourceThumbnailMaxPixelSize];
    thumbnail = CGImageSourceCreateThumbnailAtIndex(imageSource, 0, (__bridge CFDictionaryRef) info);
    
    CGImageDestinationRef destination;
    NSMutableData * destData = [NSMutableData data];
    
    destination = CGImageDestinationCreateWithData((__bridge CFMutableDataRef) destData,
                                                   (CFStringRef) @"public.jpeg",
                                                   1, NULL);
    
    CGImageDestinationAddImage(destination, thumbnail, NULL);
    CGImageDestinationFinalize(destination);
    
    CFRelease(destination);
    CFRelease(thumbnail);
    CFRelease(imageSource);
    
    return destData;
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


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

- (IBAction)favBtnPressed:(id)sender {
    if ([self.message.isFav boolValue]){
        [self.favBtn setImage:[UIImage imageNamed:@"newsFavIconUnselected"] forState:UIControlStateNormal];
        self.message.isFav = [NSNumber numberWithBool:NO];
        [self.emailListVCRef removeFromFavList:self.message sender:self.favBtn];
        [self.emailListVCRef reloadCellAtIndexPath:self.indexPath];
    }else{
        [self.favBtn setImage:[UIImage imageNamed:@"newsFavIconSelected"] forState:UIControlStateNormal];
        self.message.isFav = [NSNumber numberWithBool:YES];
        [self.emailListVCRef addToFavList:self.message sender:self.favBtn];
        [self.emailListVCRef reloadCellAtIndexPath:self.indexPath];
    }
}
@end
