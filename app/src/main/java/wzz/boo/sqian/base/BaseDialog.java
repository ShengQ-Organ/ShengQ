package wzz.boo.sqian.base;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

import wzz.boo.sqian.R;
import wzz.boo.sqian.utils.BaseViewUtils;

/**
 * 底部弹出dialog
 * @author zy
 *
 */
public class BaseDialog extends Dialog {
	private Activity context;
	private View contentView;
	public BaseDialog(Activity context) {
		this(context, Gravity.BOTTOM);
	}
	
	public BaseDialog(Activity context,int gravity) {
		super(context, R.style.bottom_dialog);
		this.context = context;
		Window window = this.getWindow();
		window.setGravity(gravity);
		window.setWindowAnimations(R.style.PopMenuAnimation);
		this.setCanceledOnTouchOutside(true);
	}

	public void setGravity(int gravity){
		Window window = this.getWindow();
		window.setGravity(gravity);
		window.setWindowAnimations(R.style.PopMenuAnimation);
	}

	@Override
	public void setContentView(View view) {
		LayoutParams params = getWindow().getAttributes();
		params.height = LayoutParams.WRAP_CONTENT;
		params.width = BaseViewUtils.getWindowsWidth(context);
		getWindow().setAttributes(params);
		contentView = view;
		super.setContentView(contentView, params);
	}
	
	private void setFullContentView(View view) {
		LayoutParams params = getWindow().getAttributes();
		params.height = BaseViewUtils.getWindowsHeight(context);
		params.width = BaseViewUtils.getWindowsWidth(context);
		getWindow().setAttributes(params);  
		super.setContentView(view, params);
	}

	private void setWarpContentView(View view) {
		LayoutParams params = getWindow().getAttributes();
		params.height = LayoutParams.WRAP_CONTENT;
		params.width = LayoutParams.WRAP_CONTENT;
		getWindow().setAttributes(params);
		super.setContentView(view, params);
	}

	@Override
	public void dismiss() {
		super.dismiss();
	}

	public void show(View contentView) {
		show();
		setContentView(contentView);
	}
	
	public void showFullScreen(View contentView) {
		show();
		setFullContentView(contentView);
	}

	public void showContentView(View contentView){
		show();
		setWarpContentView(contentView);
	}
}
