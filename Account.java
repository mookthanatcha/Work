/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sit.jpa.project.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Chonticha Sae-jiw
 */
@Entity
@Table(name = "ACCOUNT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Account a")
    , @NamedQuery(name = "Account.findByEmail", query = "SELECT a FROM Account a WHERE a.email = :email")
    , @NamedQuery(name = "Account.findByPassword", query = "SELECT a FROM Account a WHERE a.password = :password")
    , @NamedQuery(name = "Account.findByDateRegis", query = "SELECT a FROM Account a WHERE a.dateRegis = :dateRegis")
    , @NamedQuery(name = "Account.findByActivateKey", query = "SELECT a FROM Account a WHERE a.activateKey = :activateKey")
    , @NamedQuery(name = "Account.findByActivateTimestamp", query = "SELECT a FROM Account a WHERE a.activateTimestamp = :activateTimestamp")})
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "EMAIL")
    private String email;
    @Size(max = 45)
    @Column(name = "PASSWORD")
    private String password;
    @Column(name = "DATE_REGIS")
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateRegis;
    @Size(max = 45)
    @Column(name = "ACTIVATE_KEY")
    private String activateKey;
    @Column(name = "ACTIVATE_TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date activateTimestamp;
    @OneToMany(mappedBy = "email")
    private List<Customer> customerList;

    public Account() {
    }

    public Account(String email) {
        this.email = email;
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

    public Date getDateRegis() {
        return dateRegis;
    }

    public void setDateRegis(Date dateRegis) {
        this.dateRegis = dateRegis;
    }

    public String getActivateKey() {
        return activateKey;
    }

    public void setActivateKey(String activateKey) {
        this.activateKey = activateKey;
    }

    public Date getActivateTimestamp() {
        return activateTimestamp;
    }

    public void setActivateTimestamp(Date activateTimestamp) {
        this.activateTimestamp = activateTimestamp;
    }

    @XmlTransient
    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        this.customerList = customerList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (email != null ? email.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Account)) {
            return false;
        }
        Account other = (Account) object;
        if ((this.email == null && other.email != null) || (this.email != null && !this.email.equals(other.email))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "sit.jpa.project.model.Account[ email=" + email + " ]";
    }
    
}
