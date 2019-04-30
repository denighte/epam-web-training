package by.radchuk.task.util;

import lombok.Getter;

public class StringView {
    private char[] value;
    @Getter
    private int offset;
    @Getter
    private int end;
    private int hash;

    public StringView(String str) {
        value = str.toCharArray();
        offset = 0;
        end = str.length();
        hash = 0;
    }

    protected StringView(char[] source, int start, int end) {
        this.value = source;
        this.offset = start;
        this.end = end;
        this.hash = 0;
    }

    public void add(String str) {
        for (int i = 0; i < str.length(); ++i) {
            value[end + i] = str.charAt(i);
        }
        end += str.length();
        hash = 0;
    }

    public StringView sub(int start, int end) {
        return new StringView(value, start, end);
    }

    public void setEnd(int value) {
        end = value;
        hash = 0;
    }

    public void setStart(int start) {
        offset = start;
        hash = 0;
    }

    public int length() {
        return end - offset;
    }

    public int indexOf(char c) {
        for (int i = offset; i < end; i++) {
            if (value[i] == c) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Java default hashcode for <code>String</code> class.
     * @return a hash code value for this object.
     */
    @Override
    public int hashCode() {
        if (hash == 0) {
            for (int i = offset; i < end; ++i) {
                hash = 31 * hash + value[i];
            }
            return hash;
        }
        return hash;
    }

    /**
     * Indicates whether some other object is "equal to" this one.
     * @param obj the reference object with which to compare.
     * @return <code>true</code> if this object is the same as the obj
     *          argument; <code>false</code> otherwise.
     */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof StringView) {
            StringView another = (StringView) obj;
            if (length() == another.length()) {
                int start = offset;
                int anotherStart = another.offset;
                char[] a1 = this.value;
                char[] a2 = another.value;
                for (;start < end;) {
                    if (a1[start++] != a2[anotherStart++]) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return new String(value, offset, length());
    }
}

