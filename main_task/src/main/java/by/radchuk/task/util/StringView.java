package by.radchuk.task.util;

import lombok.Getter;

public class StringView {
    private char[] value;
    private int start;
    private int end;
    private int hash;

    public StringView(String str) {
        int length = str.length();
        value = new char[length + 5];
        str.getChars(0, length, value, 0);
        start = 0;
        end = length;
        hash = 0;
    }


    protected StringView(final char[] source,
                         final int start,
                         final int end) {
        this.value = source;
        this.start = start;
        this.end = end;
        this.hash = 0;
    }

    public void add(final String str) {
        for (int i = 0; i < str.length(); ++i) {
            value[end + i] = str.charAt(i);
        }
        end += str.length();
        hash = 0;
    }

    public void add(final char c) {
        value[end] = c;
        ++end;
        hash = 0;
    }

    public void add(final int n) {
        int number = n, counter = n, length = 0;
        for (;(counter /= 10) > 0; ++length);
        for(int i = end + length; i >= end; --i) {
            value[i] = (char)(number % 10 + '0');
            number /= 10;
        }
        end += length + 1;
    }

    public StringView sub(int start, int end) {
        return new StringView(value, start, end);
    }

    public void setEnd(int value) {
        end = value;
        hash = 0;
    }

    public int getEnd() {
        return end;
    }

    public void setStart(int start) {
        this.start = start;
        hash = 0;
    }

    public int getStart() {
        return start;
    }

    public int length() {
        return end - start;
    }

    public int count(char c) {
        int counter = 0;
        for (int i = start; i < end; ++i) {
            if (value[i] == c) {
                ++counter;
            }
        }
        return counter;
    }

    public int lastIndexOf(final char c) {
        for (int i = end - 1; i >= start; --i) {
            if (value[i] == c) {
                return i;
            }
        }
        return -1;
    }

    public int indexOf(final char c) {
        for (int i = start; i < end; ++i) {
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
            for (int i = start; i < end; ++i) {
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
                int start = this.start;
                int anotherStart = another.start;
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
        return new String(value, start, length());
    }
}

