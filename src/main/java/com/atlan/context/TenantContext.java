package com.atlan.context;

import lombok.Data;

@Data
public class TenantContext {
    private  static  final ThreadLocal<String> currentTenant = new ThreadLocal<>();

    public static String getTenantId(){
        return currentTenant.get();
    }
    public static  void setTenantId(String tenantId){
        currentTenant.set(tenantId);
    }
    public static void clear(){
        currentTenant.remove();
    }

}
