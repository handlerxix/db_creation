/*
 * This file is generated by jOOQ.
 */
package generated.tables.records;

import generated.tables.Company;
import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record4;
import org.jooq.Row4;
import org.jooq.impl.UpdatableRecordImpl;

/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({"all", "unchecked", "rawtypes"})
public class CompanyRecord extends UpdatableRecordImpl<CompanyRecord> implements Record4<Integer, String, String, String> {

    private static final long serialVersionUID = 1L;

    /**
     * Create a detached CompanyRecord
     */
    public CompanyRecord() {
        super(Company.COMPANY);
    }

    /**
     * Create a detached, initialised CompanyRecord
     */
    public CompanyRecord(Integer id, String name, String individualTaxNumber, String companyCheck) {
        super(Company.COMPANY);

        setId(id);
        setName(name);
        setIndividualTaxNumber(individualTaxNumber);
        setCompanyCheck(companyCheck);
    }

    /**
     * Getter for <code>public.company.id</code>.
     */
    public Integer getId() {
        return (Integer) get(0);
    }

    /**
     * Setter for <code>public.company.id</code>.
     */
    public CompanyRecord setId(Integer value) {
        set(0, value);
        return this;
    }

    /**
     * Getter for <code>public.company.name</code>.
     */
    public String getName() {
        return (String) get(1);
    }

    /**
     * Setter for <code>public.company.name</code>.
     */
    public CompanyRecord setName(String value) {
        set(1, value);
        return this;
    }

    /**
     * Getter for <code>public.company.individual_tax_number</code>.
     */
    public String getIndividualTaxNumber() {
        return (String) get(2);
    }

    /**
     * Setter for <code>public.company.individual_tax_number</code>.
     */
    public CompanyRecord setIndividualTaxNumber(String value) {
        set(2, value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * Getter for <code>public.company.company_check</code>.
     */
    public String getCompanyCheck() {
        return (String) get(3);
    }

    // -------------------------------------------------------------------------
    // Record4 type implementation
    // -------------------------------------------------------------------------

    /**
     * Setter for <code>public.company.company_check</code>.
     */
    public CompanyRecord setCompanyCheck(String value) {
        set(3, value);
        return this;
    }

    @Override
    public Record1<Integer> key() {
        return (Record1) super.key();
    }

    @Override
    public Row4<Integer, String, String, String> fieldsRow() {
        return (Row4) super.fieldsRow();
    }

    @Override
    public Row4<Integer, String, String, String> valuesRow() {
        return (Row4) super.valuesRow();
    }

    @Override
    public Field<Integer> field1() {
        return Company.COMPANY.ID;
    }

    @Override
    public Field<String> field2() {
        return Company.COMPANY.NAME;
    }

    @Override
    public Field<String> field3() {
        return Company.COMPANY.INDIVIDUAL_TAX_NUMBER;
    }

    @Override
    public Field<String> field4() {
        return Company.COMPANY.COMPANY_CHECK;
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
    public String component3() {
        return getIndividualTaxNumber();
    }

    @Override
    public String component4() {
        return getCompanyCheck();
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
    public String value3() {
        return getIndividualTaxNumber();
    }

    @Override
    public String value4() {
        return getCompanyCheck();
    }

    @Override
    public CompanyRecord value1(Integer value) {
        setId(value);
        return this;
    }

    @Override
    public CompanyRecord value2(String value) {
        setName(value);
        return this;
    }

    @Override
    public CompanyRecord value3(String value) {
        setIndividualTaxNumber(value);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    @Override
    public CompanyRecord value4(String value) {
        setCompanyCheck(value);
        return this;
    }

    @Override
    public CompanyRecord values(Integer value1, String value2, String value3, String value4) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        return this;
    }
}
