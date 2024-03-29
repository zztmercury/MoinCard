package com.lovemoin.card.app.db;

import com.lovemoin.card.app.constant.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;

import de.greenrobot.dao.DaoException;

// THIS CODE IS GENERATED BY greenDAO, EDIT ONLY INSIDE THE "KEEP"-SECTIONS
// KEEP INCLUDES - put your custom includes here
// KEEP INCLUDES END
/**
 * Entity mapped to table "ACTIVITY_INFO".
 */
public class ActivityInfo implements java.io.Serializable {

    private String activityId;
    /** Not-null value. */
    private String name;
    /** Not-null value. */
    private String brief;
    private String address;
    private Integer level;
    private String detail;
    private String img;
    private String briefImg;
    private String url;
    /** Not-null value. */
    private java.util.Date startDate;
    /** Not-null value. */
    private java.util.Date endDate;
    private Boolean isTop;
    private Boolean isOfficial;
    private Boolean isAttend;
    private Boolean isViewed;
    private int type;
    private String shareUrl;
    private Integer num;
    private String merchantId;

    /** Used to resolve relations */
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    private transient ActivityInfoDao myDao;

    private MerchantInfo merchantInfo;
    private String merchantInfo__resolvedKey;


    // KEEP FIELDS - put your custom fields here
    // KEEP FIELDS END

    public ActivityInfo() {
    }

    public ActivityInfo(String activityId) {
        this.activityId = activityId;
    }

    public ActivityInfo(String activityId, String name, String brief, String address, Integer level, String detail, String img, String briefImg, String url, java.util.Date startDate, java.util.Date endDate, Boolean isTop, Boolean isOfficial, Boolean isAttend, Boolean isViewed, int type, String shareUrl, Integer num, String merchantId) {
        this.activityId = activityId;
        this.name = name;
        this.brief = brief;
        this.address = address;
        this.level = level;
        this.detail = detail;
        this.img = img;
        this.briefImg = briefImg;
        this.url = url;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isTop = isTop;
        this.isOfficial = isOfficial;
        this.isAttend = isAttend;
        this.isViewed = isViewed;
        this.type = type;
        this.shareUrl = shareUrl;
        this.num = num;
        this.merchantId = merchantId;
    }

    /** called by internal mechanisms, do not call yourself. */
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getActivityInfoDao() : null;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    /** Not-null value. */
    public String getName() {
        return name;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setName(String name) {
        this.name = name;
    }

    /** Not-null value. */
    public String getBrief() {
        return brief;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getBriefImg() {
        return briefImg;
    }

    public void setBriefImg(String briefImg) {
        this.briefImg = briefImg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /** Not-null value. */
    public java.util.Date getStartDate() {
        return startDate;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setStartDate(java.util.Date startDate) {
        this.startDate = startDate;
    }

    /** Not-null value. */
    public java.util.Date getEndDate() {
        return endDate;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setEndDate(java.util.Date endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsTop() {
        return isTop;
    }

    public void setIsTop(Boolean isTop) {
        this.isTop = isTop;
    }

    public Boolean getIsOfficial() {
        return isOfficial;
    }

    public void setIsOfficial(Boolean isOfficial) {
        this.isOfficial = isOfficial;
    }

    public Boolean getIsAttend() {
        return isAttend;
    }

    public void setIsAttend(Boolean isAttend) {
        this.isAttend = isAttend;
    }

    public Boolean getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(Boolean isViewed) {
        this.isViewed = isViewed;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    /** To-one relationship, resolved on first access. */
    public MerchantInfo getMerchantInfo() {
        String __key = this.merchantId;
        if (merchantInfo__resolvedKey == null || merchantInfo__resolvedKey != __key) {
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MerchantInfoDao targetDao = daoSession.getMerchantInfoDao();
            MerchantInfo merchantInfoNew = targetDao.load(__key);
            synchronized (this) {
                merchantInfo = merchantInfoNew;
            	merchantInfo__resolvedKey = __key;
            }
        }
        return merchantInfo;
    }

    public void setMerchantInfo(MerchantInfo merchantInfo) {
        synchronized (this) {
            this.merchantInfo = merchantInfo;
            merchantId = merchantInfo == null ? null : merchantInfo.getMerchantUUID();
            merchantInfo__resolvedKey = merchantId;
        }
    }

    /** Convenient call for {@link AbstractDao#delete(Object)}. Entity must attached to an entity context. */
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.delete(this);
    }

    /** Convenient call for {@link AbstractDao#update(Object)}. Entity must attached to an entity context. */
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.update(this);
    }

    /** Convenient call for {@link AbstractDao#refresh(Object)}. Entity must attached to an entity context. */
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }    
        myDao.refresh(this);
    }

    // KEEP METHODS - put your custom methods here
    public ActivityInfo(JSONObject object) {
        try {
            activityId = object.getString(Config.KEY_ACTIVITY_ID);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            name = object.getString(Config.KEY_ACTIVITY_NAME);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            brief = object.getString(Config.KEY_ACTIVITY_BRIEF);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            briefImg = object.getString(Config.KEY_ACTIVITY_BRIEF_IMAGE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            type = object.getInt(Config.KEY_ACTIVITY_TYPE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            startDate = new Date(object.getLong(Config.KEY_START_DATE) * 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            endDate = new Date(object.getLong(Config.KEY_END_DATE) * 1000);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            isOfficial = object.getInt(Config.KEY_IS_OFFICIAL) == 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            isTop = object.getInt(Config.KEY_IS_TOP) == 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            isAttend = object.getInt(Config.KEY_IS_ATTEND) == 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            level = object.getInt(Config.KEY_ACTIVITY_LEVEL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            url = object.getString(Config.KEY_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            shareUrl = object.getString(Config.KEY_SHARE_URL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // KEEP METHODS END

}
