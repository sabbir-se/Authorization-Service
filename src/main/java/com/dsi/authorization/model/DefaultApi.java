package com.dsi.authorization.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * Created by sabbir on 6/24/16.
 */

@Entity
@Table(name = "dsi_default_api")
public class DefaultApi {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid2")
    @Column(name = "default_api_id", length = 40)
    private String defaultApiId;

    @ManyToOne
    @JoinColumn(name = "api_id", nullable = false)
    private Api api;

    @Column(name = "allow_type", length = 20)
    private String allowType;

    private int version;

    public String getDefaultApiId() {
        return defaultApiId;
    }

    public void setDefaultApiId(String defaultApiId) {
        this.defaultApiId = defaultApiId;
    }

    public Api getApi() {
        return api;
    }

    public void setApi(Api api) {
        this.api = api;
    }

    public String getAllowType() {
        return allowType;
    }

    public void setAllowType(String allowType) {
        this.allowType = allowType;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
