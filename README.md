#LoopView

>一个支持任意ItemView无缝循环滚动控件
>
>使用场景：纵向跑马灯、记录墙...



![](http://i.imgur.com/W5tVtNa.gif)




###如何使用？

xml文件中：
	
	 <com.ljb.loopwall.LoopWallView
        android:id="@+id/loop_view"
        android:layout_width="match_parent"
        android:layout_height="300dp"/>

Activity\Fragment中：
	
	 mLoopWallView = (LoopWallView) findViewById(R.id.loop_view);
     mLoopWallView.setScrollV(0.2f); // 设置滚动速度，值越大，速度越快（默认0.2f）
	 mLoopWallView.setAdapter(new MyAdapter(this, data));


Adapte继承BaseLoopWallAdapter类 或 实现LoopWallAdapter接口

	class MyAdapter extends BaseLoopWallAdapter<ModeItem> {

	    public MyAdapter(Context c, List<ModeItem> data) {
	        super(c, data);
	    }
	
	    @Override
	    public View getView(int position, ViewGroup parent) {
	        View view = View.inflate(getContext(), R.layout.item, null);
	        TextView tv_phone = (TextView) view.findViewById(R.id.tv_phone);
	        TextView tv_time = (TextView) view.findViewById(R.id.tv_date);
	        TextView tv_prize = (TextView) view.findViewById(R.id.tv_thing);
	
	        ModeItem item = getItem(position);
	        tv_phone.setText(item.getPhone());
	        tv_time.setText(item.getTime());
	        tv_prize.setText(item.getPrize());
	        return view;
	    }
	}