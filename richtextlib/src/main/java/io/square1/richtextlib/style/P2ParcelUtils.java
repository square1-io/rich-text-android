package io.square1.richtextlib.style;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by roberto on 12/06/15.
 */
public final class P2ParcelUtils {

    public static final Parcelable.Creator<P2ParcelableSpan> CREATOR  = new Parcelable.Creator<P2ParcelableSpan>() {

        public P2ParcelableSpan createFromParcel(Parcel in) {

            int currentPosition = in.dataPosition();
            final int type = in.readInt();
            in.setDataPosition(currentPosition);

            switch (type){
                case AbsoluteSizeSpan.TYPE:
                    return AbsoluteSizeSpan.CREATOR.createFromParcel(in);
                case AlignmentSpan.Standard.TYPE:
                    return AlignmentSpan.Standard.CREATOR.createFromParcel(in);
                case ForegroundColorSpan.TYPE:
                    return ForegroundColorSpan.CREATOR.createFromParcel(in);
                case QuoteSpan.TYPE:
                    return QuoteSpan.CREATOR.createFromParcel(in);
                case RelativeSizeSpan.TYPE:
                    return RelativeSizeSpan.CREATOR.createFromParcel(in);
                case StrikethroughSpan.TYPE:
                    return StrikethroughSpan.CREATOR.createFromParcel(in);
                case StyleSpan.TYPE:
                    return StyleSpan.CREATOR.createFromParcel(in);
                case SubscriptSpan.TYPE:
                    return SubscriptSpan.CREATOR.createFromParcel(in);
                case SuperscriptSpan.TYPE:
                    return SuperscriptSpan.CREATOR.createFromParcel(in);
                case TextAppearanceSpan.TYPE:
                    return TextAppearanceSpan.CREATOR.createFromParcel(in);
                case TypefaceSpan.TYPE:
                    return TypefaceSpan.CREATOR.createFromParcel(in);
                case UnderlineSpan.TYPE:
                    return UnderlineSpan.CREATOR.createFromParcel(in);
                case URLSpan.TYPE:
                    return URLSpan.CREATOR.createFromParcel(in);
                case BackgroundColorSpan.TYPE:
                    return BackgroundColorSpan.CREATOR.createFromParcel(in);
                case BulletSpan.TYPE:
                    return BulletSpan.CREATOR.createFromParcel(in);
            }
            return null;
        }

        public P2ParcelableSpan[] newArray(int size) {
            return new P2ParcelableSpan[size];
        }
    };

    public static void writeType(Parcel in , P2ParcelableSpan span){
        in.writeInt(span.getType());
    }

    public static void readType(Parcel in, P2ParcelableSpan span){
        int type = in.readInt();
       // span.setType(type);
    }
}
