/*
        Copyright (C) 2010-2014 Pivotal Software, Inc.


        All rights reserved. This program and the accompanying materials
        are made available under the terms of the under the Apache License,
        Version 2.0 (the "License”); you may not use this file except in compliance
        with the License. You may obtain a copy of the License at

        https://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
 */

package com.springsource.hq.plugin.tcserver.serverconfig.resources.jdbc;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import org.springframework.util.ObjectUtils;
import org.springframework.validation.Errors;

import com.springsource.hq.plugin.tcserver.serverconfig.BindableAttributes;

/**
 * Data source with a DBCP based connection pool.
 * 
 * @author Scott Andrews
 * @since 2.0
 */
@BindableAttributes
@XmlType(name = "dbcp-data-source")
public class DbcpDataSource extends DataSource {

    private DbcpConnectionPool connectionPool;

    public DbcpDataSource() {
        connectionPool = new DbcpConnectionPool();
    }

    @XmlElement(name = "connection-pool", required = true)
    public DbcpConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(DbcpConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public boolean supports(Class<?> clazz) {
        return this.getClass().equals(clazz);
    }

    public void validate(Object target, Errors errors) {
        DbcpDataSource dataSource = (DbcpDataSource) target;
        errors.pushNestedPath("general");
        dataSource.getGeneral().validate(dataSource.getGeneral(), errors);
        errors.popNestedPath();
        errors.pushNestedPath("connection");
        dataSource.getConnection().validate(dataSource.getConnection(), errors);
        errors.popNestedPath();
        errors.pushNestedPath("connectionPool");
        dataSource.getConnectionPool().validate(dataSource.getConnectionPool(), errors);
        errors.popNestedPath();
    }

    public void applyParentToChildren() {
        connectionPool.setParent(this);
        connectionPool.applyParentToChildren();
        getConnection().setParent(this);
        getConnection().applyParentToChildren();
        getGeneral().setParent(this);
        getGeneral().applyParentToChildren();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DbcpDataSource)) {
            return false;
        }
        DbcpDataSource dataSource = (DbcpDataSource) obj;
        return ObjectUtils.nullSafeEquals(this.getConnection(), dataSource.getConnection())
            && ObjectUtils.nullSafeEquals(this.getConnectionPool(), dataSource.getConnectionPool())
            && ObjectUtils.nullSafeEquals(this.getGeneral(), dataSource.getGeneral());
    }

    @Override
    public int hashCode() {
        return ObjectUtils.nullSafeHashCode(this.connectionPool) * 29 + ObjectUtils.nullSafeHashCode(this.getGeneral()) * 29
            + ObjectUtils.nullSafeHashCode(this.getConnection()) * 29;
    }

}
