
package models.db.buzz;


import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import constant.BuzzDbQueries;


@Entity
@Table(name = "ofvcard")
@NamedQueries({
        @NamedQuery(name = BuzzDbQueries.BuzzOfvcard_getUserName, query = "SELECT vcard FROM BuzzOfvcard vcard WHERE vcard.userName=:userName")
})
public class BuzzOfvcard implements Serializable
{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "username")
    String userName;

    @Column(name = "vcard")
    String vCard;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getvCard()
    {
        return vCard;
    }

    public void setvCard(String vCard)
    {
        this.vCard = vCard;
    }

}
