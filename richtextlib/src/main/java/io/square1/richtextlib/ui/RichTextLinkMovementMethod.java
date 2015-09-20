package io.square1.richtextlib.ui;

import android.text.Layout;
import android.text.Spannable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import io.square1.richtextlib.RichText;
import io.square1.richtextlib.style.ClickableSpan;

/**
 * Implementation of LinkMovementMethod to allow the loading of 
 * a link clicked inside text inside an Android application
 * without exiting to an external browser.
 * 
 * @author Isaac Whitfield
 * @version 25/08/2013
 */
 class RichTextLinkMovementMethod extends android.text.method.LinkMovementMethod {

	public interface Observer {
		void onSpansClicked(ClickableSpan[] spans);
	}


	private Observer mObserver;

	public RichTextLinkMovementMethod(Observer observer){
		super();
		mObserver = observer;
	}

	public boolean onTouchEvent(View widget, Layout layout, Spannable buffer, MotionEvent event){
		// Get the event action
		int action = event.getAction();
		
		// If action has finished
		if(action == MotionEvent.ACTION_UP) {
			// Locate the area that was pressed
			int x = (int) event.getX();
			int y = (int) event.getY();
			x -= widget.getPaddingLeft();
			y -= widget.getPaddingTop();
			x += widget.getScrollX();
			y += widget.getScrollY();
			
			// Locate the URL text
			int line = layout.getLineForVertical(y);
			int off = layout.getOffsetForHorizontal(line, x);

			// Find the URL that was pressed
			ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
			// If we've found a URL
			if (link.length != 0) {
				mObserver.onSpansClicked(link);
				return true;
			}
		}
		return false;
	}	
}