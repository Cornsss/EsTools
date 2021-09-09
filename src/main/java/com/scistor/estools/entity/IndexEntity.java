package com.scistor.estools.entity;

import lombok.Data;

/**
 * @author zhuling
 * @Description
 * @create 2021-09-06 15:27
 */
@Data
public class IndexEntity {
        private String health;

        private String status;

        private String index;

        private String uuid;

        private String pri;

        private String rep;

        private String docsCount;

        private String docsDeleted;

        private String storeSize;

        private String priStoreSize;

        @Override
        public String toString() {
                return "IndexEntity{" +
                        "health='" + health + '\'' +
                        ", status='" + status + '\'' +
                        ", index='" + index + '\'' +
                        ", uuid='" + uuid + '\'' +
                        ", pri='" + pri + '\'' +
                        ", rep='" + rep + '\'' +
                        ", docsCount='" + docsCount + '\'' +
                        ", docsDeleted='" + docsDeleted + '\'' +
                        ", storeSize='" + storeSize + '\'' +
                        ", priStoreSize='" + priStoreSize + '\'' +
                        '}';
        }
}
