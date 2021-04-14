package com.bird.cas.core.authentication.model;

import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: cas-bird
 * @description:
 * @author: JuFeng(ZhaoJun)
 * @create: 2021-04-07 14:16
 **/
public class SimplePrincipal implements Principal {


    /** The unique identifier for the principal. */
    private final String id;

    private boolean pass;

    private  final Map<String, Object> EMPTY_MAP = Collections
            .unmodifiableMap(new HashMap<String, Object>());



    /** Map of attributes for the Principal. */
    private Map<String, Object> attributes;

    public SimplePrincipal(final String id) {
        this(id,null);
    }

    public SimplePrincipal(final String id, final Map<String, Object> attributes) {
        Assert.notNull(id, "id cannot be null");
        this.id = id;

        this.attributes = attributes == null || attributes.isEmpty()
                ? EMPTY_MAP : Collections.unmodifiableMap(attributes);
    }

    public SimplePrincipal(final String id, final Map<String, Object> attributes,boolean pass) {
        Assert.notNull(id, "id cannot be null");
        this.id = id;
        this.pass = pass;

        this.attributes = attributes == null || attributes.isEmpty()
                ? EMPTY_MAP : Collections.unmodifiableMap(attributes);
    }


    public void setPass(boolean pass) {
        this.pass = pass;
    }

    @Override
    public boolean isPass() {
        return this.pass;
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    public String toString() {
        return this.id;
    }

    public int hashCode() {
        return super.hashCode() ^ this.id.hashCode();
    }

    public boolean equals(final Object o) {
        if (o == null || !this.getClass().equals(o.getClass())) {
            return false;
        }

        final SimplePrincipal p = (SimplePrincipal) o;

        return this.id.equals(p.getId());
    }
}
