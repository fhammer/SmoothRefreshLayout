package me.dkzwm.widget.srl.indicator;

import android.support.annotation.NonNull;

/**
 * @author dkzwm
 */
public class DefaultIndicator implements IIndicator {
    protected final float[] mLastMovePoint = new float[]{0f, 0f};
    protected final float[] mFingerDownPoint = new float[]{0f, 0f};
    protected int mCurrentPos = 0;
    protected int mLastPos = 0;
    protected int mHeaderHeight = -1;
    protected int mFooterHeight = -1;
    protected int mPressedPos = 0;
    protected int mRefreshCompleteY = 0;
    protected float mOffsetY;
    protected boolean mTouched = false;
    protected boolean mMoved = false;
    @MovingStatus
    protected int mStatus = MOVING_CONTENT;
    private int mOffsetToRefresh = 1;
    private int mOffsetToLoadMore = 1;
    private float mOffsetRatioToKeepHeaderWhileLoading = DEFAULT_OFFSET_RATIO_TO_KEEP_REFRESH_WHILE_LOADING;
    private float mOffsetRatioToKeepFooterWhileLoading = DEFAULT_OFFSET_RATIO_TO_KEEP_REFRESH_WHILE_LOADING;
    private float mRatioOfHeaderHeightToRefresh = DEFAULT_RATIO_OF_REFRESH_VIEW_HEIGHT_TO_REFRESH;
    private float mRatioOfFooterHeightToLoadMore = DEFAULT_RATIO_OF_REFRESH_VIEW_HEIGHT_TO_REFRESH;
    private float mCanMoveTheMaxRatioOfHeaderHeight = DEFAULT_CAN_MOVE_THE_MAX_RATIO_OF_REFRESH_VIEW_HEIGHT;
    private float mCanMoveTheMaxRatioOfFooterHeight = DEFAULT_CAN_MOVE_THE_MAX_RATIO_OF_REFRESH_VIEW_HEIGHT;
    private float mResistanceHeader = DEFAULT_RESISTANCE;
    private float mResistanceFooter = DEFAULT_RESISTANCE;

    @Override
    public boolean hasTouched() {
        return mTouched;
    }

    @Override
    public boolean hasMoved() {
        return mMoved;
    }

    @Override
    public float getResistanceOfPullDown() {
        return mResistanceHeader;
    }

    @Override
    public void setResistanceOfHeader(float resistance) {
        mResistanceHeader = resistance;
    }

    @Override
    public float getResistanceOfPullUp() {
        return mResistanceFooter;
    }

    @Override
    public void setResistanceOfFooter(float resistance) {
        mResistanceFooter = resistance;
    }

    @Override
    public void setResistance(float resistance) {
        mResistanceHeader = resistance;
        mResistanceFooter = resistance;
    }

    @Override
    public void onFingerUp() {
        mTouched = false;
        mMoved = false;
    }

    @Override
    public void onRefreshComplete() {
        mRefreshCompleteY = mCurrentPos;
    }

    @Override
    public void setRatioOfRefreshViewHeightToRefresh(float ratio) {
        mRatioOfHeaderHeightToRefresh = ratio;
        mRatioOfFooterHeightToLoadMore = ratio;
        mOffsetToRefresh = (int) (mHeaderHeight * ratio);
        mOffsetToLoadMore = (int) (mFooterHeight * ratio);
    }

    @Override
    public float getRatioOfHeaderHeightToRefresh() {
        return mRatioOfHeaderHeightToRefresh;
    }

    @Override
    public void setRatioOfHeaderHeightToRefresh(float ratio) {
        mRatioOfHeaderHeightToRefresh = ratio;
        mOffsetToRefresh = (int) (mHeaderHeight * ratio);
    }

    @Override
    public float getRatioOfFooterHeightToRefresh() {
        return mRatioOfFooterHeightToLoadMore;
    }

    @Override
    public void setRatioOfFooterHeightToRefresh(float ratio) {
        mRatioOfFooterHeightToLoadMore = ratio;
        mOffsetToLoadMore = (int) (mFooterHeight * ratio);
    }

    @Override
    public int getOffsetToRefresh() {
        return mOffsetToRefresh;
    }

    @Override
    public int getOffsetToLoadMore() {
        return mOffsetToLoadMore;
    }

    @Override
    public void onFingerDown() {
        mTouched = true;
    }

    @Override
    public void onFingerDown(float x, float y) {
        mTouched = true;
        mPressedPos = mCurrentPos;
        mLastMovePoint[0] = x;
        mLastMovePoint[1] = y;
        mFingerDownPoint[0] = x;
        mFingerDownPoint[1] = y;
    }

    @Override
    public final void onFingerMove(float x, float y) {
        mMoved = true;
        float offsetY = (y - mLastMovePoint[1]);
        processOnMove(offsetY);
        mLastMovePoint[0] = x;
        mLastMovePoint[1] = y;
    }

    @Override
    public float getOffsetY() {
        return mOffsetY;
    }

    @Override
    public int getLastPosY() {
        return mLastPos;
    }

    @Override
    public int getMovingStatus() {
        return mStatus;
    }

    @Override
    public void setMovingStatus(@MovingStatus int status) {
        mStatus = status;
    }

    @Override
    public int getCurrentPosY() {
        return mCurrentPos;
    }

    @Override
    public void setCurrentPos(int current) {
        mLastPos = mCurrentPos;
        mCurrentPos = current;
    }

    @Override
    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    @Override
    public void setHeaderHeight(int height) {
        mHeaderHeight = height;
        mOffsetToRefresh = (int) (mRatioOfHeaderHeightToRefresh * mHeaderHeight);
    }

    @Override
    public int getFooterHeight() {
        return mFooterHeight;
    }

    @Override
    public void setFooterHeight(int height) {
        mFooterHeight = height;
        mOffsetToLoadMore = (int) (mRatioOfFooterHeightToLoadMore * mFooterHeight);
    }

    @Override
    public void convert(IIndicator indicator) {
        mCurrentPos = indicator.getCurrentPosY();
        mLastPos = indicator.getLastPosY();
        mHeaderHeight = indicator.getHeaderHeight();
        mFooterHeight = indicator.getFooterHeight();
        mRatioOfHeaderHeightToRefresh = indicator.getRatioOfHeaderHeightToRefresh();
        mRatioOfFooterHeightToLoadMore = indicator.getRatioOfFooterHeightToRefresh();
        mOffsetToRefresh = indicator.getOffsetToRefresh();
        mOffsetToLoadMore = indicator.getOffsetToLoadMore();
        mResistanceHeader = indicator.getResistanceOfPullDown();
        mResistanceFooter = indicator.getResistanceOfPullUp();
    }

    @Override
    public boolean hasLeftStartPosition() {
        return mCurrentPos > START_POS;
    }

    @Override
    public boolean hasJustLeftStartPosition() {
        return mLastPos == START_POS && hasLeftStartPosition();
    }

    @Override
    public boolean hasJustBackToStartPosition() {
        return mLastPos != START_POS && isInStartPosition();
    }

    @Override
    public boolean isOverOffsetToRefresh() {
        return mCurrentPos >= getOffsetToRefresh();
    }

    @Override
    public boolean isOverOffsetToLoadMore() {
        return mCurrentPos >= getOffsetToLoadMore();
    }

    @Override
    public boolean hasMovedAfterPressedDown() {
        return mCurrentPos != mPressedPos;
    }

    @Override
    public boolean isInStartPosition() {
        return mCurrentPos == START_POS;
    }

    @Override
    public boolean crossRefreshLineFromTopToBottom() {
        return mLastPos < getOffsetToRefresh() && mCurrentPos >= getOffsetToRefresh();
    }

    @Override
    public boolean crossRefreshLineFromBottomToTop() {
        return mLastPos < getOffsetToLoadMore() && mCurrentPos >= getOffsetToLoadMore();
    }

    @Override
    public boolean hasJustReachedHeaderHeightFromTopToBottom() {
        return mLastPos < mHeaderHeight && mCurrentPos >= mHeaderHeight;
    }

    @Override
    public boolean hasJustReachedFooterHeightFromBottomToTop() {
        return mLastPos < mFooterHeight && mCurrentPos >= mFooterHeight;
    }

    @Override
    public boolean isOverOffsetToKeepHeaderWhileLoading() {
        return mCurrentPos >= getOffsetToKeepHeaderWhileLoading();
    }

    @Override
    public boolean isOverOffsetToKeepFooterWhileLoading() {
        return mCurrentPos >= getOffsetToKeepFooterWhileLoading();
    }

    @Override
    public boolean isInKeepFooterWhileLoadingPos() {
        return mCurrentPos == getOffsetToKeepFooterWhileLoading();
    }

    @Override
    public boolean isInKeepHeaderWhileLoadingPos() {
        return mCurrentPos == getOffsetToKeepHeaderWhileLoading();
    }

    @Override
    public int getOffsetToKeepHeaderWhileLoading() {
        return (int) (mOffsetRatioToKeepHeaderWhileLoading * mHeaderHeight);
    }

    @Override
    public int getOffsetToKeepFooterWhileLoading() {
        return (int) (mOffsetRatioToKeepFooterWhileLoading * mFooterHeight);
    }

    @Override
    public void setOffsetRatioToKeepFooterWhileLoading(float ratio) {
        mOffsetRatioToKeepFooterWhileLoading = ratio;
    }

    @Override
    public void setOffsetRatioToKeepHeaderWhileLoading(float ratio) {
        mOffsetRatioToKeepHeaderWhileLoading = ratio;
    }

    @Override
    public boolean isAlreadyHere(int to) {
        return mCurrentPos == to;
    }

    @Override
    public float getCurrentPercentOfRefreshOffset() {
        return mHeaderHeight <= 0 ? 0 : mCurrentPos * 1f / mOffsetToRefresh;
    }

    @Override
    public float getCurrentPercentOfLoadMoreOffset() {
        return mFooterHeight <= 0 ? 0 : mCurrentPos * 1f / mOffsetToLoadMore;
    }

    @Override
    public boolean willOverTop(int to) {
        return to < START_POS;
    }

    @Override
    public void setCanMoveTheMaxRatioOfRefreshViewHeight(float ratio) {
        setCanMoveTheMaxRatioOfHeaderHeight(ratio);
        setCanMoveTheMaxRatioOfFooterHeight(ratio);
    }

    @Override
    public float getCanMoveTheMaxRatioOfHeaderHeight() {
        return mCanMoveTheMaxRatioOfHeaderHeight;
    }

    @Override
    public void setCanMoveTheMaxRatioOfHeaderHeight(float ratio) {
        if (mCanMoveTheMaxRatioOfHeaderHeight > 0
                && mCanMoveTheMaxRatioOfHeaderHeight < mRatioOfHeaderHeightToRefresh)
            throw new RuntimeException("If mCanMoveTheMaxRatioOfHeaderHeight less than " +
                    "RatioOfHeaderHeightToRefresh, refresh will be never trigger!");
        mCanMoveTheMaxRatioOfHeaderHeight = ratio;
    }

    @Override
    public float getCanMoveTheMaxRatioOfFooterHeight() {
        return mCanMoveTheMaxRatioOfFooterHeight;
    }

    @Override
    public void setCanMoveTheMaxRatioOfFooterHeight(float ratio) {
        if (mCanMoveTheMaxRatioOfFooterHeight > 0
                && mCanMoveTheMaxRatioOfFooterHeight < mRatioOfFooterHeightToLoadMore)
            throw new RuntimeException("If MaxRatioOfFooterWhenFingerMoves less than " +
                    "RatioOfFooterHeightToLoadMore, load more will be never trigger!");
        mCanMoveTheMaxRatioOfFooterHeight = ratio;
    }

    @Override
    public float getCanMoveTheMaxDistanceOfHeader() {
        return mCanMoveTheMaxRatioOfHeaderHeight * mHeaderHeight;
    }

    @Override
    public float getCanMoveTheMaxDistanceOfFooter() {
        return mCanMoveTheMaxRatioOfFooterHeight * mFooterHeight;
    }

    @Override
    @NonNull
    public float[] getFingerDownPoint() {
        return mFingerDownPoint;
    }

    @NonNull
    @Override
    public float[] getLastMovePoint() {
        return mLastMovePoint;
    }


    private void processOnMove(float offsetY) {
        if (mStatus == MOVING_HEADER) {
            mOffsetY = offsetY / mResistanceHeader;
        } else if (mStatus == MOVING_FOOTER) {
            mOffsetY = offsetY / mResistanceFooter;
        } else if (mStatus == MOVING_CONTENT) {
            if (offsetY > 0) {
                mOffsetY = offsetY / mResistanceHeader;
            } else if (mOffsetY < 0) {
                mOffsetY = offsetY / mResistanceFooter;
            } else {
                mOffsetY = offsetY;
            }
        }
    }
}
