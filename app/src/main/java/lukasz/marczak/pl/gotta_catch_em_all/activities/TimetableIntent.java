package lukasz.marczak.pl.gotta_catch_em_all.activities;

import android.content.ClipData;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Set;

/**
 * Created by Lukasz Marczak on 2015-08-25.
 */
public abstract class TimetableIntent extends Intent {

    public abstract void putStopID(int id);
    public abstract void putStopCode(int code);
    public abstract void putStopName(String name);

    public TimetableIntent() {
        super();
    }

    public TimetableIntent(Intent o) {
        super(o);
    }

    public TimetableIntent(String action) {
        super(action);
    }

    public TimetableIntent(String action, Uri uri) {
        super(action, uri);
    }

    public TimetableIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
    }

    public TimetableIntent(String action, Uri uri, Context packageContext, Class<?> cls) {
        super(action, uri, packageContext, cls);
    }

    @NonNull
    @Override
    public Object clone() {
        return super.clone();
    }

    @NonNull
    @Override
    public Intent cloneFilter() {
        return super.cloneFilter();
    }

    @Override
    public String getAction() {
        return super.getAction();
    }

    @Override
    public Uri getData() {
        return super.getData();
    }

    @Override
    public String getDataString() {
        return super.getDataString();
    }

    @Override
    public String getScheme() {
        return super.getScheme();
    }

    @Override
    public String getType() {
        return super.getType();
    }

    @Override
    public String resolveType(Context context) {
        return super.resolveType(context);
    }

    @Override
    public String resolveType(ContentResolver resolver) {
        return super.resolveType(resolver);
    }

    @Override
    public String resolveTypeIfNeeded(ContentResolver resolver) {
        return super.resolveTypeIfNeeded(resolver);
    }

    @Override
    public boolean hasCategory(String category) {
        return super.hasCategory(category);
    }

    @Override
    public Set<String> getCategories() {
        return super.getCategories();
    }

    @Override
    public Intent getSelector() {
        return super.getSelector();
    }

    @Override
    public ClipData getClipData() {
        return super.getClipData();
    }

    @Override
    public void setExtrasClassLoader(ClassLoader loader) {
        super.setExtrasClassLoader(loader);
    }

    @Override
    public boolean hasExtra(String name) {
        return super.hasExtra(name);
    }

    @Override
    public boolean hasFileDescriptors() {
        return super.hasFileDescriptors();
    }

    @Override
    public boolean getBooleanExtra(String name, boolean defaultValue) {
        return super.getBooleanExtra(name, defaultValue);
    }

    @Override
    public byte getByteExtra(String name, byte defaultValue) {
        return super.getByteExtra(name, defaultValue);
    }

    @Override
    public short getShortExtra(String name, short defaultValue) {
        return super.getShortExtra(name, defaultValue);
    }

    @Override
    public char getCharExtra(String name, char defaultValue) {
        return super.getCharExtra(name, defaultValue);
    }

    @Override
    public int getIntExtra(String name, int defaultValue) {
        return super.getIntExtra(name, defaultValue);
    }

    @Override
    public long getLongExtra(String name, long defaultValue) {
        return super.getLongExtra(name, defaultValue);
    }

    @Override
    public float getFloatExtra(String name, float defaultValue) {
        return super.getFloatExtra(name, defaultValue);
    }

    @Override
    public double getDoubleExtra(String name, double defaultValue) {
        return super.getDoubleExtra(name, defaultValue);
    }

    @Override
    public String getStringExtra(String name) {
        return super.getStringExtra(name);
    }

    @Override
    public CharSequence getCharSequenceExtra(String name) {
        return super.getCharSequenceExtra(name);
    }

    @Override
    public <T extends Parcelable> T getParcelableExtra(String name) {
        return super.getParcelableExtra(name);
    }

    @Override
    public Parcelable[] getParcelableArrayExtra(String name) {
        return super.getParcelableArrayExtra(name);
    }

    @Override
    public <T extends Parcelable> ArrayList<T> getParcelableArrayListExtra(String name) {
        return super.getParcelableArrayListExtra(name);
    }

    @Override
    public Serializable getSerializableExtra(String name) {
        return super.getSerializableExtra(name);
    }

    @Override
    public ArrayList<Integer> getIntegerArrayListExtra(String name) {
        return super.getIntegerArrayListExtra(name);
    }

    @Override
    public ArrayList<String> getStringArrayListExtra(String name) {
        return super.getStringArrayListExtra(name);
    }

    @Override
    public ArrayList<CharSequence> getCharSequenceArrayListExtra(String name) {
        return super.getCharSequenceArrayListExtra(name);
    }

    @Override
    public boolean[] getBooleanArrayExtra(String name) {
        return super.getBooleanArrayExtra(name);
    }

    @Override
    public byte[] getByteArrayExtra(String name) {
        return super.getByteArrayExtra(name);
    }

    @Override
    public short[] getShortArrayExtra(String name) {
        return super.getShortArrayExtra(name);
    }

    @Override
    public char[] getCharArrayExtra(String name) {
        return super.getCharArrayExtra(name);
    }

    @Override
    public int[] getIntArrayExtra(String name) {
        return super.getIntArrayExtra(name);
    }

    @Override
    public long[] getLongArrayExtra(String name) {
        return super.getLongArrayExtra(name);
    }

    @Override
    public float[] getFloatArrayExtra(String name) {
        return super.getFloatArrayExtra(name);
    }

    @Override
    public double[] getDoubleArrayExtra(String name) {
        return super.getDoubleArrayExtra(name);
    }

    @Override
    public String[] getStringArrayExtra(String name) {
        return super.getStringArrayExtra(name);
    }

    @Override
    public CharSequence[] getCharSequenceArrayExtra(String name) {
        return super.getCharSequenceArrayExtra(name);
    }

    @Override
    public Bundle getBundleExtra(String name) {
        return super.getBundleExtra(name);
    }

    @Override
    public Bundle getExtras() {
        return super.getExtras();
    }

    @Override
    public int getFlags() {
        return super.getFlags();
    }

    @Override
    public String getPackage() {
        return super.getPackage();
    }

    @Override
    public ComponentName getComponent() {
        return super.getComponent();
    }

    @Override
    public Rect getSourceBounds() {
        return super.getSourceBounds();
    }

    @Override
    public ComponentName resolveActivity(PackageManager pm) {
        return super.resolveActivity(pm);
    }

    @Override
    public ActivityInfo resolveActivityInfo(PackageManager pm, int flags) {
        return super.resolveActivityInfo(pm, flags);
    }

    @NonNull
    @Override
    public Intent setAction(String action) {
        return super.setAction(action);
    }

    @NonNull
    @Override
    public Intent setData(Uri data) {
        return super.setData(data);
    }

    @NonNull
    @Override
    public Intent setDataAndNormalize(Uri data) {
        return super.setDataAndNormalize(data);
    }

    @NonNull
    @Override
    public Intent setType(String type) {
        return super.setType(type);
    }

    @NonNull
    @Override
    public Intent setTypeAndNormalize(String type) {
        return super.setTypeAndNormalize(type);
    }

    @NonNull
    @Override
    public Intent setDataAndType(Uri data, String type) {
        return super.setDataAndType(data, type);
    }

    @NonNull
    @Override
    public Intent setDataAndTypeAndNormalize(Uri data, String type) {
        return super.setDataAndTypeAndNormalize(data, type);
    }

    @NonNull
    @Override
    public Intent addCategory(String category) {
        return super.addCategory(category);
    }

    @Override
    public void removeCategory(String category) {
        super.removeCategory(category);
    }

    @Override
    public void setSelector(Intent selector) {
        super.setSelector(selector);
    }

    @Override
    public void setClipData(ClipData clip) {
        super.setClipData(clip);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, boolean value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, byte value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, char value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, short value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, int value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, long value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, float value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, double value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, String value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, CharSequence value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, Parcelable value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, Parcelable[] value) {
        return super.putExtra(name, value);
    }

    @Override
    public Intent putParcelableArrayListExtra(String name, ArrayList<? extends Parcelable> value) {
        return super.putParcelableArrayListExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putIntegerArrayListExtra(String name, ArrayList<Integer> value) {
        return super.putIntegerArrayListExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putStringArrayListExtra(String name, ArrayList<String> value) {
        return super.putStringArrayListExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putCharSequenceArrayListExtra(String name, ArrayList<CharSequence> value) {
        return super.putCharSequenceArrayListExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, Serializable value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, boolean[] value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, byte[] value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, short[] value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, char[] value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, int[] value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, long[] value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, float[] value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, double[] value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, String[] value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, CharSequence[] value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtra(String name, Bundle value) {
        return super.putExtra(name, value);
    }

    @NonNull
    @Override
    public Intent putExtras(Intent src) {
        return super.putExtras(src);
    }

    @NonNull
    @Override
    public Intent putExtras(Bundle extras) {
        return super.putExtras(extras);
    }

    @NonNull
    @Override
    public Intent replaceExtras(Intent src) {
        return super.replaceExtras(src);
    }

    @NonNull
    @Override
    public Intent replaceExtras(Bundle extras) {
        return super.replaceExtras(extras);
    }

    @Override
    public void removeExtra(String name) {
        super.removeExtra(name);
    }

    @NonNull
    @Override
    public Intent setFlags(int flags) {
        return super.setFlags(flags);
    }

    @NonNull
    @Override
    public Intent addFlags(int flags) {
        return super.addFlags(flags);
    }

    @NonNull
    @Override
    public Intent setPackage(String packageName) {
        return super.setPackage(packageName);
    }

    @NonNull
    @Override
    public Intent setComponent(ComponentName component) {
        return super.setComponent(component);
    }

    @NonNull
    @Override
    public Intent setClassName(Context packageContext, String className) {
        return super.setClassName(packageContext, className);
    }

    @NonNull
    @Override
    public Intent setClassName(String packageName, String className) {
        return super.setClassName(packageName, className);
    }

    @NonNull
    @Override
    public Intent setClass(Context packageContext, Class<?> cls) {
        return super.setClass(packageContext, cls);
    }

    @Override
    public void setSourceBounds(Rect r) {
        super.setSourceBounds(r);
    }

    @Override
    public int fillIn(Intent other, int flags) {
        return super.fillIn(other, flags);
    }

    @Override
    public boolean filterEquals(Intent other) {
        return super.filterEquals(other);
    }

    @Override
    public int filterHashCode() {
        return super.filterHashCode();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @NonNull
    @Override
    public String toURI() {
        return super.toURI();
    }

    @NonNull
    @Override
    public String toUri(int flags) {
        return super.toUri(flags);
    }

    @Override
    public int describeContents() {
        return super.describeContents();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
    }

    @Override
    public void readFromParcel(Parcel in) {
        super.readFromParcel(in);
    }
}
