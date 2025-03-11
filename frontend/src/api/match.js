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
    console.log(response.data);
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

    // 백엔드에서 204 No Content를 반환할 경우, response.data가 undefined일 수 있음
    if (!response.data) {
      console.warn(`경기 ${matchId}의 리뷰가 없습니다.`);
      return []; // 빈 배열 반환
    }

    // 데이터가 배열인지 확인 후 처리
    if (Array.isArray(response.data)) {
      return response.data.map((review) => ({
        id: review.reviewId,
        rating: review.rating,
        comment: review.comment,
      }));
    } else {
      console.warn('서버 응답 데이터가 배열이 아님:', response.data);
      return [];
    }
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
    // 서버 응답에서 reviewId를 id로 매핑
    return {
      id: response.data.reviewId, // reviewId를 id로 매핑
      rating: response.data.rating,
      comment: response.data.comment,
    };
  } catch (error) {
    console.error('리뷰 작성 중 오류 발생:', error);
    throw error;
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

// 팀별 경기 데이터 가져오기
export const fetchMatchesByTeam = async (teamId) => {
  try {
    // Try multiple possible endpoint formats
    let response;

    try {
      // Option 1: RESTful path parameter
      response = await axios.get(`${API_BASE_URL}/team/${teamId}`);
    } catch (firstError) {
      console.warn('First attempt failed, trying alternative endpoint format');

      try {
        // Option 2: Query parameter
        response = await axios.get(`${API_BASE_URL}/team`, {
          params: { teamName: teamId },
        });
      } catch (secondError) {
        console.warn('Second attempt failed, trying final endpoint format');

        // Option 3: Different endpoint name based on Java method
        response = await axios.get(`${API_BASE_URL}/by-team`, {
          params: { teamName: teamId },
        });
      }
    }

    console.log('Team matches API response:', response.data);
    return response.data;
  } catch (error) {
    console.error('팀별 경기 데이터를 불러오는 중 오류 발생:', error);
    console.error('Error details:', error.response?.data || error.message);
    return [];
  }
};
