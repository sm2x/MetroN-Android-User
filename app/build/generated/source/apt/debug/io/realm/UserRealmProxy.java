package io.realm;


import android.util.JsonReader;
import android.util.JsonToken;
import com.tronline.user.Models.User;

import io.realm.exceptions.RealmMigrationNeededException;
import io.realm.internal.ColumnType;
import io.realm.internal.ImplicitTransaction;
import io.realm.internal.RealmObjectProxy;
import io.realm.internal.Table;
import io.realm.internal.TableOrView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class UserRealmProxy extends User
    implements RealmObjectProxy {

    private static long INDEX_ID;
    private static long INDEX_FNAME;
    private static long INDEX_LNAME;
    private static long INDEX_EMAIL;
    private static long INDEX_PROFILEURL;
    private static long INDEX_PHONE;
    private static long INDEX_PASSWORD;
    private static long INDEX_GENDER;
    private static long INDEX_COUNTRY;
    private static long INDEX_REFERRALCODE;
    private static long INDEX_REFERRALBONUS;
    private static long INDEX_CURRENCY;
    private static long INDEX_USER_LANGUAGE;
    private static Map<String, Long> columnIndices;
    private static final List<String> FIELD_NAMES;
    static {
        List<String> fieldNames = new ArrayList<String>();
        fieldNames.add("id");
        fieldNames.add("fname");
        fieldNames.add("lname");
        fieldNames.add("email");
        fieldNames.add("profileurl");
        fieldNames.add("phone");
        fieldNames.add("password");
        fieldNames.add("gender");
        fieldNames.add("country");
        fieldNames.add("referralCode");
        fieldNames.add("referralBonus");
        fieldNames.add("currency");
        fieldNames.add("user_language");
        FIELD_NAMES = Collections.unmodifiableList(fieldNames);
    }

    @Override
    public int getId() {
        realm.checkIfValid();
        return (int) row.getLong(INDEX_ID);
    }

    @Override
    public void setId(int value) {
        realm.checkIfValid();
        row.setLong(INDEX_ID, (long) value);
    }

    @Override
    public String getFname() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_FNAME);
    }

    @Override
    public void setFname(String value) {
        realm.checkIfValid();
        row.setString(INDEX_FNAME, (String) value);
    }

    @Override
    public String getLname() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_LNAME);
    }

    @Override
    public void setLname(String value) {
        realm.checkIfValid();
        row.setString(INDEX_LNAME, (String) value);
    }

    @Override
    public String getEmail() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_EMAIL);
    }

    @Override
    public void setEmail(String value) {
        realm.checkIfValid();
        row.setString(INDEX_EMAIL, (String) value);
    }

    @Override
    public String getProfileurl() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_PROFILEURL);
    }

    @Override
    public void setProfileurl(String value) {
        realm.checkIfValid();
        row.setString(INDEX_PROFILEURL, (String) value);
    }

    @Override
    public String getPhone() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_PHONE);
    }

    @Override
    public void setPhone(String value) {
        realm.checkIfValid();
        row.setString(INDEX_PHONE, (String) value);
    }

    @Override
    public String getPassword() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_PASSWORD);
    }

    @Override
    public void setPassword(String value) {
        realm.checkIfValid();
        row.setString(INDEX_PASSWORD, (String) value);
    }

    @Override
    public String getGender() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_GENDER);
    }

    @Override
    public void setGender(String value) {
        realm.checkIfValid();
        row.setString(INDEX_GENDER, (String) value);
    }

    @Override
    public String getCountry() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_COUNTRY);
    }

    @Override
    public void setCountry(String value) {
        realm.checkIfValid();
        row.setString(INDEX_COUNTRY, (String) value);
    }

    @Override
    public String getReferralCode() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_REFERRALCODE);
    }

    @Override
    public void setReferralCode(String value) {
        realm.checkIfValid();
        row.setString(INDEX_REFERRALCODE, (String) value);
    }

    @Override
    public String getReferralBonus() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_REFERRALBONUS);
    }

    @Override
    public void setReferralBonus(String value) {
        realm.checkIfValid();
        row.setString(INDEX_REFERRALBONUS, (String) value);
    }

    @Override
    public String getCurrency() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_CURRENCY);
    }

    @Override
    public void setCurrency(String value) {
        realm.checkIfValid();
        row.setString(INDEX_CURRENCY, (String) value);
    }

    @Override
    public String getUser_language() {
        realm.checkIfValid();
        return (java.lang.String) row.getString(INDEX_USER_LANGUAGE);
    }

    @Override
    public void setUser_language(String value) {
        realm.checkIfValid();
        row.setString(INDEX_USER_LANGUAGE, (String) value);
    }

    public static Table initTable(ImplicitTransaction transaction) {
        if (!transaction.hasTable("class_User")) {
            Table table = transaction.getTable("class_User");
            table.addColumn(ColumnType.INTEGER, "id");
            table.addColumn(ColumnType.STRING, "fname");
            table.addColumn(ColumnType.STRING, "lname");
            table.addColumn(ColumnType.STRING, "email");
            table.addColumn(ColumnType.STRING, "profileurl");
            table.addColumn(ColumnType.STRING, "phone");
            table.addColumn(ColumnType.STRING, "password");
            table.addColumn(ColumnType.STRING, "gender");
            table.addColumn(ColumnType.STRING, "country");
            table.addColumn(ColumnType.STRING, "referralCode");
            table.addColumn(ColumnType.STRING, "referralBonus");
            table.addColumn(ColumnType.STRING, "currency");
            table.addColumn(ColumnType.STRING, "user_language");
            table.addSearchIndex(table.getColumnIndex("id"));
            table.setPrimaryKey("id");
            return table;
        }
        return transaction.getTable("class_User");
    }

    public static void validateTable(ImplicitTransaction transaction) {
        if (transaction.hasTable("class_User")) {
            Table table = transaction.getTable("class_User");
            if (table.getColumnCount() != 13) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Field count does not match - expected 13 but was " + table.getColumnCount());
            }
            Map<String, ColumnType> columnTypes = new HashMap<String, ColumnType>();
            for (long i = 0; i < 13; i++) {
                columnTypes.put(table.getColumnName(i), table.getColumnType(i));
            }

            columnIndices = new HashMap<String, Long>();
            for (String fieldName : getFieldNames()) {
                long index = table.getColumnIndex(fieldName);
                if (index == -1) {
                    throw new RealmMigrationNeededException(transaction.getPath(), "Field '" + fieldName + "' not found for type User");
                }
                columnIndices.put(fieldName, index);
            }
            INDEX_ID = table.getColumnIndex("id");
            INDEX_FNAME = table.getColumnIndex("fname");
            INDEX_LNAME = table.getColumnIndex("lname");
            INDEX_EMAIL = table.getColumnIndex("email");
            INDEX_PROFILEURL = table.getColumnIndex("profileurl");
            INDEX_PHONE = table.getColumnIndex("phone");
            INDEX_PASSWORD = table.getColumnIndex("password");
            INDEX_GENDER = table.getColumnIndex("gender");
            INDEX_COUNTRY = table.getColumnIndex("country");
            INDEX_REFERRALCODE = table.getColumnIndex("referralCode");
            INDEX_REFERRALBONUS = table.getColumnIndex("referralBonus");
            INDEX_CURRENCY = table.getColumnIndex("currency");
            INDEX_USER_LANGUAGE = table.getColumnIndex("user_language");

            if (!columnTypes.containsKey("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'id'");
            }
            if (columnTypes.get("id") != ColumnType.INTEGER) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'int' for field 'id'");
            }
            if (table.getPrimaryKey() != table.getColumnIndex("id")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Primary key not defined for field 'id'");
            }
            if (!table.hasSearchIndex(table.getColumnIndex("id"))) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Index not defined for field 'id'");
            }
            if (!columnTypes.containsKey("fname")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'fname'");
            }
            if (columnTypes.get("fname") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'fname'");
            }
            if (!columnTypes.containsKey("lname")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'lname'");
            }
            if (columnTypes.get("lname") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'lname'");
            }
            if (!columnTypes.containsKey("email")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'email'");
            }
            if (columnTypes.get("email") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'email'");
            }
            if (!columnTypes.containsKey("profileurl")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'profileurl'");
            }
            if (columnTypes.get("profileurl") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'profileurl'");
            }
            if (!columnTypes.containsKey("phone")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'phone'");
            }
            if (columnTypes.get("phone") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'phone'");
            }
            if (!columnTypes.containsKey("password")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'password'");
            }
            if (columnTypes.get("password") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'password'");
            }
            if (!columnTypes.containsKey("gender")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'gender'");
            }
            if (columnTypes.get("gender") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'gender'");
            }
            if (!columnTypes.containsKey("country")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'country'");
            }
            if (columnTypes.get("country") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'country'");
            }
            if (!columnTypes.containsKey("referralCode")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'referralCode'");
            }
            if (columnTypes.get("referralCode") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'referralCode'");
            }
            if (!columnTypes.containsKey("referralBonus")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'referralBonus'");
            }
            if (columnTypes.get("referralBonus") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'referralBonus'");
            }
            if (!columnTypes.containsKey("currency")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'currency'");
            }
            if (columnTypes.get("currency") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'currency'");
            }
            if (!columnTypes.containsKey("user_language")) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Missing field 'user_language'");
            }
            if (columnTypes.get("user_language") != ColumnType.STRING) {
                throw new RealmMigrationNeededException(transaction.getPath(), "Invalid type 'String' for field 'user_language'");
            }
        } else {
            throw new RealmMigrationNeededException(transaction.getPath(), "The User class is missing from the schema for this Realm.");
        }
    }

    public static String getTableName() {
        return "class_User";
    }

    public static List<String> getFieldNames() {
        return FIELD_NAMES;
    }

    public static Map<String,Long> getColumnIndices() {
        return columnIndices;
    }

    public static User createOrUpdateUsingJsonObject(Realm realm, JSONObject json, boolean update)
        throws JSONException {
        User obj = null;
        if (update) {
            Table table = realm.getTable(User.class);
            long pkColumnIndex = table.getPrimaryKey();
            if (!json.isNull("id")) {
                long rowIndex = table.findFirstLong(pkColumnIndex, json.getLong("id"));
                if (rowIndex != TableOrView.NO_MATCH) {
                    obj = new UserRealmProxy();
                    obj.realm = realm;
                    obj.row = table.getUncheckedRow(rowIndex);
                }
            }
        }
        if (obj == null) {
            obj = realm.createObject(User.class);
        }
        if (!json.isNull("id")) {
            obj.setId((int) json.getInt("id"));
        }
        if (json.has("fname")) {
            if (json.isNull("fname")) {
                obj.setFname("");
            } else {
                obj.setFname((String) json.getString("fname"));
            }
        }
        if (json.has("lname")) {
            if (json.isNull("lname")) {
                obj.setLname("");
            } else {
                obj.setLname((String) json.getString("lname"));
            }
        }
        if (json.has("email")) {
            if (json.isNull("email")) {
                obj.setEmail("");
            } else {
                obj.setEmail((String) json.getString("email"));
            }
        }
        if (json.has("profileurl")) {
            if (json.isNull("profileurl")) {
                obj.setProfileurl("");
            } else {
                obj.setProfileurl((String) json.getString("profileurl"));
            }
        }
        if (json.has("phone")) {
            if (json.isNull("phone")) {
                obj.setPhone("");
            } else {
                obj.setPhone((String) json.getString("phone"));
            }
        }
        if (json.has("password")) {
            if (json.isNull("password")) {
                obj.setPassword("");
            } else {
                obj.setPassword((String) json.getString("password"));
            }
        }
        if (json.has("gender")) {
            if (json.isNull("gender")) {
                obj.setGender("");
            } else {
                obj.setGender((String) json.getString("gender"));
            }
        }
        if (json.has("country")) {
            if (json.isNull("country")) {
                obj.setCountry("");
            } else {
                obj.setCountry((String) json.getString("country"));
            }
        }
        if (json.has("referralCode")) {
            if (json.isNull("referralCode")) {
                obj.setReferralCode("");
            } else {
                obj.setReferralCode((String) json.getString("referralCode"));
            }
        }
        if (json.has("referralBonus")) {
            if (json.isNull("referralBonus")) {
                obj.setReferralBonus("");
            } else {
                obj.setReferralBonus((String) json.getString("referralBonus"));
            }
        }
        if (json.has("currency")) {
            if (json.isNull("currency")) {
                obj.setCurrency("");
            } else {
                obj.setCurrency((String) json.getString("currency"));
            }
        }
        if (json.has("user_language")) {
            if (json.isNull("user_language")) {
                obj.setUser_language("");
            } else {
                obj.setUser_language((String) json.getString("user_language"));
            }
        }
        return obj;
    }

    public static User createUsingJsonStream(Realm realm, JsonReader reader)
        throws IOException {
        User obj = realm.createObject(User.class);
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals("id") && reader.peek() != JsonToken.NULL) {
                obj.setId((int) reader.nextInt());
            } else if (name.equals("fname")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setFname("");
                    reader.skipValue();
                } else {
                    obj.setFname((String) reader.nextString());
                }
            } else if (name.equals("lname")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setLname("");
                    reader.skipValue();
                } else {
                    obj.setLname((String) reader.nextString());
                }
            } else if (name.equals("email")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setEmail("");
                    reader.skipValue();
                } else {
                    obj.setEmail((String) reader.nextString());
                }
            } else if (name.equals("profileurl")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setProfileurl("");
                    reader.skipValue();
                } else {
                    obj.setProfileurl((String) reader.nextString());
                }
            } else if (name.equals("phone")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setPhone("");
                    reader.skipValue();
                } else {
                    obj.setPhone((String) reader.nextString());
                }
            } else if (name.equals("password")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setPassword("");
                    reader.skipValue();
                } else {
                    obj.setPassword((String) reader.nextString());
                }
            } else if (name.equals("gender")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setGender("");
                    reader.skipValue();
                } else {
                    obj.setGender((String) reader.nextString());
                }
            } else if (name.equals("country")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setCountry("");
                    reader.skipValue();
                } else {
                    obj.setCountry((String) reader.nextString());
                }
            } else if (name.equals("referralCode")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setReferralCode("");
                    reader.skipValue();
                } else {
                    obj.setReferralCode((String) reader.nextString());
                }
            } else if (name.equals("referralBonus")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setReferralBonus("");
                    reader.skipValue();
                } else {
                    obj.setReferralBonus((String) reader.nextString());
                }
            } else if (name.equals("currency")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setCurrency("");
                    reader.skipValue();
                } else {
                    obj.setCurrency((String) reader.nextString());
                }
            } else if (name.equals("user_language")) {
                if (reader.peek() == JsonToken.NULL) {
                    obj.setUser_language("");
                    reader.skipValue();
                } else {
                    obj.setUser_language((String) reader.nextString());
                }
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return obj;
    }

    public static User copyOrUpdate(Realm realm, User object, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        if (object.realm != null && object.realm.getPath().equals(realm.getPath())) {
            return object;
        }
        User realmObject = null;
        boolean canUpdate = update;
        if (canUpdate) {
            Table table = realm.getTable(User.class);
            long pkColumnIndex = table.getPrimaryKey();
            long rowIndex = table.findFirstLong(pkColumnIndex, object.getId());
            if (rowIndex != TableOrView.NO_MATCH) {
                realmObject = new UserRealmProxy();
                realmObject.realm = realm;
                realmObject.row = table.getUncheckedRow(rowIndex);
                cache.put(object, (RealmObjectProxy) realmObject);
            } else {
                canUpdate = false;
            }
        }

        if (canUpdate) {
            return update(realm, realmObject, object, cache);
        } else {
            return copy(realm, object, update, cache);
        }
    }

    public static User copy(Realm realm, User newObject, boolean update, Map<RealmObject,RealmObjectProxy> cache) {
        User realmObject = realm.createObject(User.class, newObject.getId());
        cache.put(newObject, (RealmObjectProxy) realmObject);
        realmObject.setId(newObject.getId());
        realmObject.setFname(newObject.getFname() != null ? newObject.getFname() : "");
        realmObject.setLname(newObject.getLname() != null ? newObject.getLname() : "");
        realmObject.setEmail(newObject.getEmail() != null ? newObject.getEmail() : "");
        realmObject.setProfileurl(newObject.getProfileurl() != null ? newObject.getProfileurl() : "");
        realmObject.setPhone(newObject.getPhone() != null ? newObject.getPhone() : "");
        realmObject.setPassword(newObject.getPassword() != null ? newObject.getPassword() : "");
        realmObject.setGender(newObject.getGender() != null ? newObject.getGender() : "");
        realmObject.setCountry(newObject.getCountry() != null ? newObject.getCountry() : "");
        realmObject.setReferralCode(newObject.getReferralCode() != null ? newObject.getReferralCode() : "");
        realmObject.setReferralBonus(newObject.getReferralBonus() != null ? newObject.getReferralBonus() : "");
        realmObject.setCurrency(newObject.getCurrency() != null ? newObject.getCurrency() : "");
        realmObject.setUser_language(newObject.getUser_language() != null ? newObject.getUser_language() : "");
        return realmObject;
    }

    static User update(Realm realm, User realmObject, User newObject, Map<RealmObject, RealmObjectProxy> cache) {
        realmObject.setFname(newObject.getFname() != null ? newObject.getFname() : "");
        realmObject.setLname(newObject.getLname() != null ? newObject.getLname() : "");
        realmObject.setEmail(newObject.getEmail() != null ? newObject.getEmail() : "");
        realmObject.setProfileurl(newObject.getProfileurl() != null ? newObject.getProfileurl() : "");
        realmObject.setPhone(newObject.getPhone() != null ? newObject.getPhone() : "");
        realmObject.setPassword(newObject.getPassword() != null ? newObject.getPassword() : "");
        realmObject.setGender(newObject.getGender() != null ? newObject.getGender() : "");
        realmObject.setCountry(newObject.getCountry() != null ? newObject.getCountry() : "");
        realmObject.setReferralCode(newObject.getReferralCode() != null ? newObject.getReferralCode() : "");
        realmObject.setReferralBonus(newObject.getReferralBonus() != null ? newObject.getReferralBonus() : "");
        realmObject.setCurrency(newObject.getCurrency() != null ? newObject.getCurrency() : "");
        realmObject.setUser_language(newObject.getUser_language() != null ? newObject.getUser_language() : "");
        return realmObject;
    }

    @Override
    public String toString() {
        if (!isValid()) {
            return "Invalid object";
        }
        StringBuilder stringBuilder = new StringBuilder("User = [");
        stringBuilder.append("{id:");
        stringBuilder.append(getId());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{fname:");
        stringBuilder.append(getFname());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{lname:");
        stringBuilder.append(getLname());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{email:");
        stringBuilder.append(getEmail());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{profileurl:");
        stringBuilder.append(getProfileurl());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{phone:");
        stringBuilder.append(getPhone());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{password:");
        stringBuilder.append(getPassword());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{gender:");
        stringBuilder.append(getGender());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{country:");
        stringBuilder.append(getCountry());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{referralCode:");
        stringBuilder.append(getReferralCode());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{referralBonus:");
        stringBuilder.append(getReferralBonus());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{currency:");
        stringBuilder.append(getCurrency());
        stringBuilder.append("}");
        stringBuilder.append(",");
        stringBuilder.append("{user_language:");
        stringBuilder.append(getUser_language());
        stringBuilder.append("}");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Override
    public int hashCode() {
        String realmName = realm.getPath();
        String tableName = row.getTable().getName();
        long rowIndex = row.getIndex();

        int result = 17;
        result = 31 * result + ((realmName != null) ? realmName.hashCode() : 0);
        result = 31 * result + ((tableName != null) ? tableName.hashCode() : 0);
        result = 31 * result + (int) (rowIndex ^ (rowIndex >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRealmProxy aUser = (UserRealmProxy)o;

        String path = realm.getPath();
        String otherPath = aUser.realm.getPath();
        if (path != null ? !path.equals(otherPath) : otherPath != null) return false;;

        String tableName = row.getTable().getName();
        String otherTableName = aUser.row.getTable().getName();
        if (tableName != null ? !tableName.equals(otherTableName) : otherTableName != null) return false;

        if (row.getIndex() != aUser.row.getIndex()) return false;

        return true;
    }

}
