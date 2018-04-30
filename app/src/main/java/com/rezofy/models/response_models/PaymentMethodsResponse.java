package com.rezofy.models.response_models;

import java.util.List;

/**
 * Created by linchpin on 2/2/17.
 */

public class PaymentMethodsResponse {

    List<Gateways> gateways;


    public List<Gateways> getGateways() {
        return gateways;
    }

    public void setGateways(List<Gateways> gateways) {
        this.gateways = gateways;
    }

    public class Gateways
    {
        String id, name, params, status, isDefault;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getParams() {
            return params;
        }

        public void setParams(String params) {
            this.params = params;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIsDefault() {
            return isDefault;
        }

        public void setIsDefault(String isDefault) {
            this.isDefault = isDefault;
        }
    }
}
