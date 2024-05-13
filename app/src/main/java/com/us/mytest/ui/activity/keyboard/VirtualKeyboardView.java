package com.us.mytest.ui.activity.keyboard;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Vibrator;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.us.mytest.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 虚拟键盘
 */
public class VirtualKeyboardView extends RelativeLayout {

    private Context context;

    private ArrayList<Map<String, String>> valueList = new ArrayList<>();
    //因为要用Adapter中适配，用数组不能往adapter中填充
    private Animation enterAnim;
    private Animation exitAnim;
    private EditText targetEditText;
    private Vibrator vibrator;

    public VirtualKeyboardView(Context context) {
        this(context, null);
    }

    public VirtualKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAnimAndVibrator();
        initValueList();
        addView(initKeyBoardView());
    }

    @NonNull
    private View initKeyBoardView() {
        View view = View.inflate(context, R.layout.layout_virtual_keyboard, null);
        view.findViewById(R.id.layoutBack).setOnClickListener(v -> closeEditText());
        GridView gridView = (GridView) view.findViewById(R.id.gv_keybord);
        KeyBoardAdapter keyBoardAdapter = new KeyBoardAdapter(context, valueList);
        gridView.setAdapter(keyBoardAdapter);
        gridView.setOnItemClickListener(onItemClickListener);
        return view;
    }

    private void initValueList() {
        // 初始化按钮上应该显示的数字
        for (int i = 1; i < 13; i++) {
            Map<String, String> map = new HashMap<>();
            if (i < 10) {
                map.put("name", String.valueOf(i));
            } else if (i == 10) {
                map.put("name", ".");
            } else if (i == 11) {
                map.put("name", String.valueOf(0));
            } else {
                map.put("name", "");
            }
            valueList.add(map);
        }
    }

    //键盘的出入动画
    private void initAnimAndVibrator() {
        enterAnim = AnimationUtils.loadAnimation(context, R.anim.push_bottom_in_key_board);
        exitAnim = AnimationUtils.loadAnimation(context, R.anim.push_bottom_out_key_board);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    //外部先调用一下  directory
    public void changeShowEditText(Activity activity, EditText editText) {
        //锁定目标输入框
        targetEditText = editText;
        // 设置不调用系统键盘
        if (android.os.Build.VERSION.SDK_INT <= 10) {
            editText.setInputType(InputType.TYPE_NULL);
        } else {
            activity.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus",
                        boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(editText, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //外部目标输入框点击事件
    public void showEditText() {
        if (isShow()) return;
        setFocusable(true);
        setFocusableInTouchMode(true);
        startAnimation(enterAnim);
        setVisibility(View.VISIBLE);
    }

    public void closeEditText() {
        startAnimation(exitAnim);
        setVisibility(View.GONE);
    }

    public boolean isShow() {
        return getVisibility() == VISIBLE;
    }

    //键盘点击监听
    private final AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            vibrator.vibrate(30);
            if (position < 11 && position != 9) {//点击0~9按钮
                String amount = targetEditText.getText().toString().trim();
                amount = amount + valueList.get(position).get("name");
                targetEditText.setText(amount);
                Editable ea = targetEditText.getText();
                targetEditText.setSelection(ea.length());
            } else {
                if (position == 9) {//点击退格键
                    String amount = targetEditText.getText().toString().trim();
                    if (!amount.contains(".")) {
                        amount = amount + valueList.get(position).get("name");
                        targetEditText.setText(amount);

                        Editable ea = targetEditText.getText();
                        targetEditText.setSelection(ea.length());
                    }
                }
                if (position == 11) {//点击退格键
                    String amount = targetEditText.getText().toString().trim();
                    if (amount.length() > 0) {
                        amount = amount.substring(0, amount.length() - 1);
                        targetEditText.setText(amount);

                        Editable ea = targetEditText.getText();
                        targetEditText.setSelection(ea.length());
                    }
                }
            }
        }
    };

    //键盘的listView
    static class KeyBoardAdapter extends BaseAdapter {
        private final Context mContext;
        private final ArrayList<Map<String, String>> valueList;

        public KeyBoardAdapter(Context mContext, ArrayList<Map<String, String>> valueList) {
            this.mContext = mContext;
            this.valueList = valueList;
        }

        @Override
        public int getCount() {
            return valueList.size();
        }

        @Override
        public Object getItem(int position) {
            return valueList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            KeyViewHolder viewHolder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_virtual_keyboard, null);
                viewHolder = new KeyViewHolder();
                viewHolder.btnKey = (TextView) convertView.findViewById(R.id.btn_keys);
                viewHolder.imgDelete = (RelativeLayout) convertView.findViewById(R.id.imgDelete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (KeyViewHolder) convertView.getTag();
            }
            if (position == 9) {
                viewHolder.imgDelete.setVisibility(View.INVISIBLE);
                viewHolder.btnKey.setVisibility(View.VISIBLE);
                viewHolder.btnKey.setText(valueList.get(position).get("name"));
                viewHolder.btnKey.setBackgroundColor(Color.parseColor("#e0e0e0"));
            } else if (position == 11) {
                viewHolder.btnKey.setBackgroundResource(R.drawable.keyboard_delete_img);
                viewHolder.imgDelete.setVisibility(View.VISIBLE);
                viewHolder.btnKey.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.imgDelete.setVisibility(View.INVISIBLE);
                viewHolder.btnKey.setVisibility(View.VISIBLE);
                viewHolder.btnKey.setText(valueList.get(position).get("name"));
            }
            return convertView;
        }

        static final class KeyViewHolder {
            public TextView btnKey;
            public RelativeLayout imgDelete;
        }
    }
}
