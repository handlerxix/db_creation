/*
 * This file is generated by jOOQ.
 */
package generated.tables.records;

import generated.tables.WaybillProducts;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record5;
import org.jooq.Row5;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class WaybillProductsRecord extends UpdatableRecordImpl<WaybillProductsRecord> implements Record5<Integer, Integer, Integer, Integer, Integer> {

    private static final long serialVersionUID = 1L;

    /**
     * Create a detached WaybillProductsRecord
     */
    public WaybillProductsRecord() {
        super(WaybillProducts.WAYBILL_PRODUCTS);
    }

    /**
     * Create a detached, initialised WaybillProductsRecord
     */
    public WaybillProductsRecord(Integer id, Integer waybillId, Integer productId, Integer price, Integer count) {
        super(WaybillProducts.WAYBILL_PRODUCTS);

        setId(id);
        setWaybillId(waybillId);
        setProductId(productId);
        setPrice(price);
        setCount(count);
    }

    /**
     * Getter for <code>public.waybill_products.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.waybill_products.id</code>.
     */
    public WaybillProductsRecord setId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.waybill_products.waybill_id</code>.
     */
    public Integer getWaybillId() {
        return (Integer) get(1);
    }

    /**
     * Setter for <code>public.waybill_products.waybill_id</code>.
     */
    public WaybillProductsRecord setWaybillId(Integer value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.waybill_products.product_id</code>.
     */
    public Integer getProductId() {
        return (Integer) get(2);
    }

    /**
     * Setter for <code>public.waybill_products.product_id</code>.
     */
    public WaybillProductsRecord setProductId(Integer value) {
        set(2, value);
        return this;
    }

    /**
     * Getter for <code>public.waybill_products.price</code>.
     */
    public Integer getPrice() {
        return (Integer) get(3);
    }

    /**
     * Setter for <code>public.waybill_products.price</code>.
     */
    public WaybillProductsRecord setPrice(Integer value) {
        set(3, value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>public.waybill_products.count</code>.
     */
    public Integer getCount() {
        return (Integer) get(4);
    }

    // -------------------------------------------------------------------------
    // Record5 type implementation
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>public.waybill_products.count</code>.
     */
    public WaybillProductsRecord setCount(Integer value) {
        set(4, value);
        return this;
    }

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    @Override
    public Row5<Integer, Integer, Integer, Integer, Integer> fieldsRow() {
        return (Row5) super.fieldsRow();
    }

    @Override
    public Row5<Integer, Integer, Integer, Integer, Integer> valuesRow() {
        return (Row5) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return WaybillProducts.WAYBILL_PRODUCTS.ID;
    }

    @Override
    public Field<Integer> field2() {
        return WaybillProducts.WAYBILL_PRODUCTS.WAYBILL_ID;
    }

    @Override
    public Field<Integer> field3() {
        return WaybillProducts.WAYBILL_PRODUCTS.PRODUCT_ID;
    }

    @Override
    public Field<Integer> field4() {
        return WaybillProducts.WAYBILL_PRODUCTS.PRICE;
    }

    @Override
    public Field<Integer> field5() {
        return WaybillProducts.WAYBILL_PRODUCTS.COUNT;
    }

    @Override
    public Integer component1() {
        return getId();
    }

    @Override
    public Integer component2() {
        return getWaybillId();
    }

    @Override
    public Integer component3() {
        return getProductId();
    }

    @Override
    public Integer component4() {
        return getPrice();
    }

    @Override
    public Integer component5() {
        return getCount();
    }

    @Override
    public Integer value1() {
        return getId();
    }

    @Override
    public Integer value2() {
        return getWaybillId();
    }

    @Override
    public Integer value3() {
        return getProductId();
    }

    @Override
    public Integer value4() {
        return getPrice();
    }

    @Override
    public Integer value5() {
        return getCount();
    }

    @Override
    public WaybillProductsRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public WaybillProductsRecord value2(Integer value) {
        setWaybillId(value);
        return this;
    }

    @Override
    public WaybillProductsRecord value3(Integer value) {
        setProductId(value);
        return this;
    }

    @Override
    public WaybillProductsRecord value4(Integer value) {
        setPrice(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @Override
    public WaybillProductsRecord value5(Integer value) {
        setCount(value);
        return this;
    }

    @Override
    public WaybillProductsRecord values(Integer value1, Integer value2, Integer value3, Integer value4, Integer value5) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        return this;
    }
}