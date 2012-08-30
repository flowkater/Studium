package com.fragment;

import java.util.ArrayList;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.activity.R;
import com.adapter.PartymListAdapter;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.model.Partymessage;
import com.model.Partymessages;
import com.utils.Global;
import com.utils.NetHelper;

public class PartyMessagesFragment extends SherlockFragment implements
		OnItemClickListener {
	private PartymListAdapter mAdapter;
	private ArrayList<Partymessage> mArrayList;
	private View mView;
	private PullToRefreshListView mListView;
	private Integer mCurrentPage = 1;
	int mPrevTotalItemCount = 0;
	private String mResult;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.partymessages_list, container, false);
		mArrayList = new ArrayList<Partymessage>();
		mListView = (PullToRefreshListView) mView
				.findViewById(R.id.party_message_list);
		// new GetPartymList().execute();

		// 비트맵 예제 임의 생성
		// BitmapFactory.Options options = new BitmapFactory.Options();
		// options.inSampleSize = 4;
		// Bitmap orgImage = BitmapFactory.decodeResource(getResources(),
		// R.drawable.member_byung, options);
		// Bitmap resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		// mArrayList
		// .add(new Partymessage(
		// resize,
		// "영어회화스터디",
		// "영어회화 능력향상",
		// "미정",
		// "회화 스터디 수준은 가리지 않고 회화 연습을 하고 서로에게 자극이 되고 공부하는 데 도움될 수 있는 스터디를 구하고 싶습니다..스터디를 하게 되면 열심히 의욕적으로 빠지지 않고 참여하려고 생각하고 있구요, 서로 도움이 되었으면 합니다.혹시 스터디 멤버 충원하실 의향 있으신 분은 쪽지 주세요~ "));
		// orgImage = BitmapFactory.decodeResource(getResources(),
		// R.drawable.member_jobs, options);
		// resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		// mArrayList
		// .add(new Partymessage(
		// resize,
		// "하반기 금융공기업 재무관리",
		// "스터디 동안에 최소 재무관리 전 범위를 4회독 이상 목표로 하고 있습니다.",
		// "경영대 이명박 라운지",
		// "하반기 금융권 공기업 전형일정이 몇 달 남지 않았기 때문에, 약간 타이트하게 진행할 예정입니다. 스터디 동안에 최소 재무관리 전 범위를 4회독 이상 목표로 하고 있습니다. 다음주 목요일까지 스터디원 충원 받겠습니다"));
		// orgImage = BitmapFactory.decodeResource(getResources(),
		// R.drawable.member_ladygaga, options);
		// resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		// mArrayList
		// .add(new Partymessage(resize,"취업스터디 모집합니당!", "취업!!!!!",
		// "토요일 10시~13시 3시간, 장소: 백기 스터디룸",
		// "이번 하반기를 목표로 취업을 준비하는, 자소서와 면접위주로 스터디에 참여 할, (가장 중요!)끝까지 함께 할, 스터디원을 모집합니다."));
		// orgImage = BitmapFactory.decodeResource(getResources(),
		// R.drawable.member_picture, options);
		// resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		// mArrayList
		// .add(new Partymessage(
		// resize,
		// "취업 인적성 스터디 인원 충원합니다! [인문/경상 계열]",
		// "인적성 모의고사 및 영역별 학습 및 인적성 스터디",
		// "미정 학교쪽으로.",
		// "안녕하세요, 하반기 공채를 맞아,  인적성 스터디 인원 3명을 충원하려 합니다! 첫 모임은 다음주 월요일(로 할 예정이구요, 첫 모임에서는 의견 교환, 교재 선정을 할 예정입니다. 현재, 재학생 2명(8월 27일) 통계, 경제(2중) / 영문,국제(2중), 졸업생 1명(영문, 정치외교(2중)이 있구요 상반기 사트 유경험자가 한분 있습니다."));
		// orgImage = BitmapFactory.decodeResource(getResources(),
		// R.drawable.member_wall, options);
		// resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		// mArrayList
		// .add(new Partymessage(
		// resize,
		// "종합 PD 스터디 팀원을 새롭게 모집하고자 합니다",
		// "PD 공채",
		// "장소는 이화여대 ECC 세미나룸 모임시간은 화요일 15:30 ~ 18:30, 금요일 10:30 ~ 13:30",
		// "현재 두 명 모두 졸업을 앞둔 대학생입니다. 누구보다도 간절하고 절박한 심정으로 스터디를 시작하고자 합니다. 앞으로 다가올 공채에 도전하실, 열정있으신 분들과 함께 할 수 있으면 좋겠습니다. 예비 졸업생이시거나, 졸업생이신 분들과 스터디 모임을 진행할 예정입니다. 구체적인 스터디 커리큘럼은 지원해주신 분들과 함께 첫 모임에서 확정짓겠습니다. 다양한 공부를 압축적으로 해야하는 만큼, 짜임새있는 커리큘럼으로 효율적인 스터디를 진행할 생각입니다"));
		// orgImage = BitmapFactory.decodeResource(getResources(),
		// R.drawable.member_ya, options);
		// resize = Bitmap.createScaledBitmap(orgImage, 70, 70, true);
		// mArrayList
		// .add(new Partymessage(
		// resize,
		// "남은방학기간동안 스터디하실분!",
		// "개강전 워밍업!",
		// "매일 아침 중광",
		// "일주일좀넘게남았네요 개강전 워밍업차원에서 매일아침 아홉시 중광에서 출첵하고 자유공부하고 같이 점심먹구 저녁까지함께할 생활스터디원 구해요! 현재는저 혼자구용 토욜부터 바로시작합니당 보증금 만원~ 개강후 돌려드리거나 개강파티원하시면 그비용이될수도 ㅜ 한분만 오셔도 오붓하게둘이 ㅜ.ㅜ"));

		mListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				mArrayList.clear();
				mCurrentPage = 1;
				mPrevTotalItemCount = 0;
				// new GetPartymList().execute();
				// mListView.onRefreshComplete();
			}
		});
		mAdapter = new PartymListAdapter(mView.getContext(),
				R.layout.partymessages_row, mArrayList);
		mListView.getRefreshableView().setAdapter(mAdapter);
		mListView.getRefreshableView().setOnItemClickListener(this);
		mListView.getRefreshableView().setOnScrollListener(
				new EndlessScrollListener());
		return mView;
	}

	private class GetPartymList extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			mResult = NetHelper.DownloadHtml(Global.ServerUrl
					+ "groups/partym.json?page=" + mCurrentPage.toString());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			Gson gson = new Gson();
			Partymessages partyms = gson.fromJson(mResult, Partymessages.class);
			for (Partymessage partym : partyms.getPartymessages()) {
				if (!partym.getBody().equals("")) {
					mArrayList.add(partym);
				}
			}
			mCurrentPage++;
			mListView.onRefreshComplete();
			mAdapter.notifyDataSetChanged();

			super.onPostExecute(result);
		}
	}

	public class EndlessScrollListener implements OnScrollListener {

		private int visibleThreshold = 5;
		private boolean loading = true;

		public EndlessScrollListener() {
		}

		public EndlessScrollListener(int visibleThreshold) {
			this.visibleThreshold = visibleThreshold;
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

			if (loading) {
				if (totalItemCount > mPrevTotalItemCount) {
					loading = false;
					mPrevTotalItemCount = totalItemCount;
				}
			}
			if (!loading
					&& (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
				new GetPartymList().execute();
				loading = true;
			}
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
		}
	}

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
}
