package com.netsol.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;


public class UserModel  implements Parcelable {

	public String userName;
	public String email;
	public String password;
	public String subject;
	public String messagedataTime;
	public String toUserName;
	public String fromUserName;
    public String colorCode;
    public String message;
    public String messageID;
    public String isFavourite;
    public String isRead;

	public UserModel() {
		super();

	}

	public UserModel( String userName, String email, String password, String messagedataTime) {
		super();


		this.userName = userName;
		this.email = email;
		this.password = password;
		this.messagedataTime = messagedataTime;

	}
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(subject);
        out.writeString(messagedataTime);
        out.writeString(toUserName);
        out.writeString(fromUserName);
        out.writeString(colorCode);
        out.writeString(message);
        out.writeString(messageID);
        out.writeString(isFavourite);
        out.writeString(isRead);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private UserModel(Parcel in) {
        subject = in.readString();
        messagedataTime = in.readString();
        toUserName = in.readString();
        fromUserName = in.readString();
        colorCode = in.readString();
        message = in.readString();
        messageID = in.readString();
        isFavourite = in.readString();
        isRead = in.readString();


    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getStatus() {
		return subject;
	}
	public void setStatus(String subject) {
		this.subject = subject;
	}

	public String getDataTime() {
		return messagedataTime;
	}
	public void setDataTime(String messagedataTime) {
		this.messagedataTime = messagedataTime;
	}

    public String getToUserName() {
        return toUserName;
    }

    public String getFromUserName() {
        return fromUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public void setFromUserName(String fromUserName) {
        this.fromUserName = fromUserName;
    }

    public String getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }
}
