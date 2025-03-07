import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/matches';

// 월별 경기 데이터 가져오기
export const fetchMatchesByMonth = async (year, month) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/month`, {
      params: { year, month },
    });
    return response.data;
  } catch (error) {
    console.error('경기 데이터를 불러오는 중 오류 발생:', error);
    return [];
  }
};

// 특정 경기 상세 정보 가져오기
export const fetchMatchById = async (matchId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${matchId}`);
    return response.data;
  } catch (error) {
    console.error('경기 상세 데이터를 불러오는 중 오류 발생:', error);
    return null;
  }
};

// 유튜브 하이라이트 영상 가져오기
export const fetchYoutubeHighlight = async (matchId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${matchId}/youtube`);
    return response.data.youtubeUrl;
  } catch (error) {
    console.error('유튜브 하이라이트를 불러오는 중 오류 발생:', error);
    return null;
  }
};

// 관련 뉴스 가져오기
export const fetchMatchNews = async (matchId) => {
  try {
    const response = await axios.get(`${API_BASE_URL}/${matchId}/news`);
    return response.data.items; // 뉴스 목록 반환
  } catch (error) {
    console.error('경기 관련 뉴스를 불러오는 중 오류 발생:', error);
    return [];
  }
};

// 특정 경기의 리뷰 가져오기
export const fetchMatchReviews = async (matchId) => {
  if (!matchId) {
    console.error('matchId가 유효하지 않습니다.');
    return [];
  }
  try {
    const response = await axios.get(`${API_BASE_URL}/${matchId}/reviews`);
    // 응답 데이터에 id가 포함되어 있는지 확인하고 매핑
    return response.data.map((review) => ({
      id: review.reviewId,
      rating: review.rating,
      comment: review.comment,
      // 필요한 경우 다른 필드들도 추가
    }));
  } catch (error) {
    console.error('경기 리뷰를 불러오는 중 오류 발생:', error);
    return [];
  }
};

// 리뷰 등록하기
export const submitReview = async (matchId, rating, comment) => {
  try {
    const response = await axios.post(`${API_BASE_URL}/${matchId}/reviews`, {
      rating,
      comment,
    });
    // 서버 응답에서 review 객체를 반환
    return {
      id: response.data.id,
      rating: response.data.rating,
      comment: response.data.comment,
    };
  } catch (error) {
    console.error('리뷰 작성 중 오류 발생:', error);
    throw error; // 에러를 상위로 전파하여 적절한 처리가 가능하도록 함
  }
};

// 리뷰 수정하기
export const updateReview = async (matchId, reviewId, rating, comment) => {
  try {
    const response = await axios.put(
      `${API_BASE_URL}/${matchId}/reviews/${reviewId}`,
      {
        rating,
        comment,
      }
    );
    return response.data;
  } catch (error) {
    console.error('리뷰 수정 중 오류 발생:', error);
  }
};

// 리뷰 삭제하기
export const deleteReview = async (matchId, reviewId) => {
  if (!matchId || !reviewId) {
    console.error('삭제할 reviewId가 유효하지 않음', reviewId);
    throw new Error('Invalid matchId or reviewId');
  }

  try {
    await axios.delete(`${API_BASE_URL}/${matchId}/reviews/${reviewId}`);
  } catch (error) {
    console.error('리뷰 삭제 실패:', error);
    throw error;
  }
};
