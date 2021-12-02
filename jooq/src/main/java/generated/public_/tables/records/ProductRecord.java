/*
 * This file is generated by jOOQ.
 */
package generated.public_.tables.records;

import generated.public_.tables.Product;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record2;
import org.jooq.Row2;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class ProductRecord extends UpdatableRecordImpl<ProductRecord> implements Record2<Integer, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Create a detached ProductRecord
     */
    public ProductRecord() {
        super(Product.PRODUCT);
    }

    /**
     * Create a detached, initialised ProductRecord
     */
    public ProductRecord(Integer id, String name) {
        super(Product.PRODUCT);

        setId(id);
        setName(name);
    }

    /**
     * Getter for <code>public.product.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.product.id</code>.
     */
    public ProductRecord setId(Integer value) {
        set(0, value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>public.product.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    // -------------------------------------------------------------------------
    // Record2 type implementation
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>public.product.name</code>.
     */
    public ProductRecord setName(String value) {
        set(1, value);
        return this;
    }

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    @Override
    public Row2<Integer, String> fieldsRow() {
        return (Row2) super.fieldsRow();
    }

    @Override
    public Row2<Integer, String> valuesRow() {
        return (Row2) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Product.PRODUCT.ID;
    }

    @Override
    public Field<String> field2() {
        return Product.PRODUCT.NAME;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public String component2() {
        return getName();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public String value2() {
        return getName();
    }

    @Override
    public ProductRecord value1(Integer value) {
        setId(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @Override
    public ProductRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public ProductRecord values(Integer value1, String value2) {
        value1(value1);
        value2(value2);
        return this;
    }
}