package io.square1.richtextlib.style;

import android.os.Parcel;
import android.os.Parcelable;

import io.square1.richtextlib.util.Utils;

/**
 * Created by roberto on 12/06/15.
 */
public final class P2ParcelUtils {

//    public static final Parcelable.Creator<P2ParcelableSpan> CREATOR  = new Parcelable.Creator<P2ParcelableSpan>() {
//
//        public P2ParcelableSpan createFromParcel(Parcel in) {
//
//        //    String type = in.readString();
//        //    P2ParcelableSpan span = Utils.newInstance(type);
//        //    span.readFromParcel(in);
//       //     return span;
//
//    //        int currentPosition = in.dataPosition();
//    //        final int type = in.readInt();
//    //        in.setDataPosition(currentPosition);
//
////
////            if(type ==  AbsoluteSizeSpan.TYPE)
////                return AbsoluteSizeSpan.CREATOR.createFromParcel(in);
////            if(type ==  RichAlignmentSpan.Standard.TYPE)
////                return RichAlignmentSpan.Standard.CREATOR.createFromParcel(in);
////            if(type ==  ForegroundColorSpan.TYPE)
////                return ForegroundColorSpan.CREATOR.createFromParcel(in);
////            if(type ==  QuoteSpan.TYPE)
////                return QuoteSpan.CREATOR.createFromParcel(in);
////            if(type ==  RelativeSizeSpan.TYPE)
////                return RelativeSizeSpan.CREATOR.createFromParcel(in);
////            if(type ==  StrikethroughSpan.TYPE)
////                return StrikethroughSpan.CREATOR.createFromParcel(in);
////            if(type ==  StyleSpan.TYPE)
////                return StyleSpan.CREATOR.createFromParcel(in);
////            if(type ==  SubscriptSpan.TYPE)
////                return SubscriptSpan.CREATOR.createFromParcel(in);
////            if(type ==  SuperscriptSpan.TYPE)
////                return SuperscriptSpan.CREATOR.createFromParcel(in);
////            if(type ==  TextAppearanceSpan.TYPE)
////                return TextAppearanceSpan.CREATOR.createFromParcel(in);
////            if(type ==  TypefaceSpan.TYPE)
////                return TypefaceSpan.CREATOR.createFromParcel(in);
////            if(type ==  UnderlineSpan.TYPE)
////                return UnderlineSpan.CREATOR.createFromParcel(in);
////            if(type ==  URLSpan.TYPE)
////                return URLSpan.CREATOR.createFromParcel(in);
////            if(type ==  BackgroundColorSpan.TYPE)
////                return BackgroundColorSpan.CREATOR.createFromParcel(in);
////            if(type ==  BulletSpan.TYPE)
////                return BulletSpan.CREATOR.createFromParcel(in);
////            if(type ==  BitmapSpan.TYPE)
////                return BitmapSpan.CREATOR.createFromParcel(in);
////            if(type == UrlBitmapSpan.TYPE)
////                return UrlBitmapSpan.CREATOR.createFromParcel(in);
////            if(type == YouTubeSpan.TYPE)
////                return YouTubeSpan.CREATOR.createFromParcel(in);
////            if(type == LeadingMarginSpan.TYPE)
////                return LeadingMarginSpan.CREATOR.createFromParcel(in);
//
//  //          return null;
//        }
//
//        public P2ParcelableSpan[] newArray(int size) {
//            return new P2ParcelableSpan[size];
//        }
//    };

    public static void writeType(Parcel in , P2ParcelableSpan span){
       // in.writeString(span.getClass().getName());
    }


}
