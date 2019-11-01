package com.denchion.b2b;

import android.view.View;

public class NLevelItem implements NLevelListItem {
	
	private Object wrappedObject;
	private Object wrappedObject2;
	private Object wrappedObject3;
	private NLevelItem parent;
	private NLevelView nLevelView;
	private boolean isExpanded = false;
	
	public NLevelItem(Object wrappedObject,Object wrappedObject2, Object wrappedObject3,NLevelItem parent, NLevelView nLevelView) {
		this.wrappedObject = wrappedObject;
		this.wrappedObject2 = wrappedObject2;
		this.wrappedObject3 = wrappedObject3;
		this.parent = parent;
		this.nLevelView = nLevelView;
	}
	
	public Object getWrappedObject() {
		return wrappedObject;
	}
	
	public Object getWrappedObject2() {
		return wrappedObject2;
	}
	
	public Object getWrappedObject3() {
		return wrappedObject3;
	}
	
	@Override
	public boolean isExpanded() {
		return isExpanded;
	}
	@Override
	public NLevelListItem getParent() {
		return parent;
	}
	@Override
	public View getView() {
		return nLevelView.getView(this);
	}
	@Override
	public void toggle() {
		isExpanded = !isExpanded;
	}
}
