//
//  MCTTableViewCell.m
//  iOS UI Test
//
//  Created by Paul Young on 14/07/2013.
//  Copyright (c) 2013 AppJon. All rights reserved.
//

#import "MCTTableViewCell.h"
#import <QuartzCore/QuartzCore.h>

@implementation MCTTableViewCell

- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:UITableViewCellStyleSubtitle reuseIdentifier:reuseIdentifier];
    return self;
}

- (void)prepareForReuse{
    [self.messageRenderingOperation cancel];
    [self.cirleLbl.layer setCornerRadius:8];
}

-(void) makeCellRead{
    [self.bigSubjectLbl setFont:[UIFont systemFontOfSize:14]];
    [self.detailLbl setFont:[UIFont systemFontOfSize:14]];
}

-(void) makeCellUnread{
    [self.bigSubjectLbl setFont:[UIFont boldSystemFontOfSize:15]];
    [self.detailLbl setFont:[UIFont boldSystemFontOfSize:15]];
}

- (IBAction)favBtnPressed:(id)sender {
    if ([self.message.isFav boolValue]){
        [self.favBtn setImage:[UIImage imageNamed:@"newsFavIconUnselected"] forState:UIControlStateNormal];
        [self.delegate removeFromFavList:self.message sender:self.favBtn];
    }else{
        [self.favBtn setImage:[UIImage imageNamed:@"newsFavIconSelected"] forState:UIControlStateNormal];
        [self.delegate addToFavList:self.message sender:self.favBtn];
    }
    
//    [self.delegate reloadCellAtIndexPath:self.indexPath];
}
@end
