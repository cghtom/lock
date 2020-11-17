package po;

import java.util.Date;

public class Item {
    private Long id;

    private Date gmtCreate;

    private Date gmtUpdate;

    private Integer number;

    private Integer version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtUpdate() {
        return gmtUpdate;
    }

    public void setGmtUpdate(Date gmtUpdate) {
        this.gmtUpdate = gmtUpdate;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

	@Override
	public String toString() {
		return "Item [id=" + id + ", gmtCreate=" + gmtCreate + ", gmtUpdate=" + gmtUpdate + ", number=" + number
				+ ", version=" + version + "]";
	}
    
}