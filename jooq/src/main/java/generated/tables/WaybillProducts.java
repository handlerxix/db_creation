/*
 * This file is generated by jOOQ.
 */
package generated.tables;

import generated.Keys;
import generated.Public;
import generated.tables.records.WaybillProductsRecord;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;
import org.jooq.impl.TableImpl;

import java.util.Arrays;
import java.util.List;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class WaybillProducts extends TableImpl<WaybillProductsRecord> {

    /**
     * The reference instance of <code>public.waybill_products</code>
     */
    public static final WaybillProducts WAYBILL_PRODUCTS = new WaybillProducts();
    private static final long serialVersionUID = 1L;
    /**
     * The column <code>public.waybill_products.id</code>.
     */
    public final TableField<WaybillProductsRecord, Integer> ID = createField(DSL.name("id"), SQLDataType.INTEGER.nullable(false).identity(true), this, "");
    /**
     * The column <code>public.waybill_products.waybill_id</code>.
     */
    public final TableField<WaybillProductsRecord, Integer> WAYBILL_ID = createField(DSL.name("waybill_id"), SQLDataType.INTEGER.nullable(false), this, "");
    /**
     * The column <code>public.waybill_products.product_id</code>.
     */
    public final TableField<WaybillProductsRecord, Integer> PRODUCT_ID = createField(DSL.name("product_id"), SQLDataType.INTEGER.nullable(false), this, "");
    /**
     * The column <code>public.waybill_products.price</code>.
     */
    public final TableField<WaybillProductsRecord, Integer> PRICE = createField(DSL.name("price"), SQLDataType.INTEGER.nullable(false), this, "");
    /**
     * The column <code>public.waybill_products.count</code>.
     */
    public final TableField<WaybillProductsRecord, Integer> COUNT = createField(DSL.name("count"), SQLDataType.INTEGER.nullable(false), this, "");
    private transient Waybill _waybill;
    private transient Product _product;

    /**
     * Create an aliased <code>public.waybill_products</code> table reference
     */
    public WaybillProducts(String alias) {
        this(DSL.name(alias), WAYBILL_PRODUCTS);
    }

    /**
     * Create an aliased <code>public.waybill_products</code> table reference
     */
    public WaybillProducts(Name alias) {
        this(alias, WAYBILL_PRODUCTS);
    }

    /**
     * Create a <code>public.waybill_products</code> table reference
     */
    public WaybillProducts() {
        this(DSL.name("waybill_products"), null);
    }

    public <O extends Record> WaybillProducts(Table<O> child, ForeignKey<O, WaybillProductsRecord> key) {
        super(child, key, WAYBILL_PRODUCTS);
    }

    private WaybillProducts(Name alias, Table<WaybillProductsRecord> aliased) {
        this(alias, aliased, null);
    }

    private WaybillProducts(Name alias, Table<WaybillProductsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, DSL.comment(""), TableOptions.table());
    }

    /**
     * The class holding records for this type
     */
    @Override
    public Class<WaybillProductsRecord> getRecordType() {
        return WaybillProductsRecord.class;
    }

    @Override
    public Schema getSchema() {
        return aliased() ? null : Public.PUBLIC;
    }

    @Override
    public Identity<WaybillProductsRecord, Integer> getIdentity() {
        return (Identity<WaybillProductsRecord, Integer>) super.getIdentity();
    }

    @Override
    public UniqueKey<WaybillProductsRecord> getPrimaryKey() {
        return Keys.WAYBILL_PRODUCTS_PK;
    }

    @Override
    public List<ForeignKey<WaybillProductsRecord, ?>> getReferences() {
        return Arrays.asList(Keys.WAYBILL_PRODUCTS__WAYBILL_PRODUCTS_WAYBILL_ID_FKEY, Keys.WAYBILL_PRODUCTS__WAYBILL_PRODUCTS_PRODUCT_ID_FKEY);
    }

    public Waybill waybill() {
        if (_waybill == null)
            _waybill = new Waybill(this, Keys.WAYBILL_PRODUCTS__WAYBILL_PRODUCTS_WAYBILL_ID_FKEY);

        return _waybill;
    }

    public Product product() {
        if (_product == null)
            _product = new Product(this, Keys.WAYBILL_PRODUCTS__WAYBILL_PRODUCTS_PRODUCT_ID_FKEY);

        return _product;
    }

    @Override
    public WaybillProducts as(String alias) {
        return new WaybillProducts(DSL.name(alias), this);
    }

    @Override
    public WaybillProducts as(Name alias) {
        return new WaybillProducts(alias, this);
    }

    /**
     * Rename this table
     */
    @Override
    public WaybillProducts rename(String name) {
        return new WaybillProducts(DSL.name(name), null);
    }

    /**
     * Rename this table
     */
    @Override
    public WaybillProducts rename(Name name) {
        return new WaybillProducts(name, null);
    }

    // -------------------------------------------------------------------------
    // Row5 type methods
    // -------------------------------------------------------------------------

    @Override
    public Row5<Integer, Integer, Integer, Integer, Integer> fieldsRow() {
        return (Row5) super.fieldsRow();
    }
}