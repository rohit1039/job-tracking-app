import {
  DISPLAY_ALERT,
  CLEAR_ALERT,
  SETUP_USER_SUCCESS, SETUP_USER_ERROR, SETUP_USER_BEGIN,
  TOGGLE_SIDEBAR, LOGOUT_USER,
  UPDATE_USER_BEGIN, UPDATE_USER_SUCCESS, UPDATE_USER_ERROR,
  CREATE_JOB_ERROR, CREATE_JOB_SUCCESS, CREATE_JOB_BEGIN,
  GET_JOBS_BEGIN, GET_JOBS_SUCCESS, GET_JOBS_ERROR,
  HANDLE_CHANGE, CLEAR_VALUES, SHOW_STATS_SUCCESS,
  SHOW_STATS_BEGIN, SHOW_MONTHLY_APPS_BEGIN, SHOW_MONTHLY_APPS_SUCCESS,
  GET_ALL_USERS_BEGIN, GET_ALL_USERS_SUCCESS, GET_ALL_USERS_ERROR, GET_CURRENT_USER_BEGIN,
  GET_CURRENT_USER_SUCCESS, CLEAR_FILTERS, CHANGE_PAGE,
  GET_USER_BEGIN, GET_USER_SUCCESS, SHOW_MONTHLY_APPS_ERROR, SHOW_STATS_ERROR, USERS_DATA_BEGIN,
  USERS_DATA_SUCCESS,
  GET_JOBS_ON_SEARCH_BEGIN,
  GET_JOBS_ON_SEARCH_SUCCESS,
  GET_JOBS_ON_SEARCH_ERROR,
  GET_USERS_ON_SEARCH_BEGIN,
  GET_USERS_ON_SEARCH_SUCCESS,
  GET_USERS_ON_SEARCH_ERROR
} from './actions';
import { initialState } from './appContext';

const reducer = (state, action) => {
  if (action.type === DISPLAY_ALERT) {
    return {
      ...state,
      showAlert: true,
      alertType: 'danger',
      alertText: 'Fields cannot be empty!',
    };
  }
  if (action.type === CLEAR_ALERT) {
    return {
      ...state,
      showAlert: false,
      alertType: '',
      alertText: '',
    };
  }
  if (action.type === SETUP_USER_BEGIN) {
    return { ...state, isLoading: true, showAlert: false }
  }
  if (action.type === SETUP_USER_SUCCESS) {
    return {
      ...state,
      firstName: action.payload.firstName,
      lastName: action.payload.lastName,
      emailID: action.payload.emailID,
      password: action.payload.password,
      location: action.payload.location,
      token: action.payload.token,
      jobId: action.payload.jobId,
      jobType: action.payload.jobType,
      status: action.payload.status,
      jobs: action.payload.jobs,
      userId: action.payload.userId,
      company: action.payload.company,
      position: action.payload.position,
      jobLocation: action.payload.jobLocation,
      fullName: action.payload.fullName,
      age: action.payload.age,
      isLoading: false,
      showAlert: true,
      alertType: 'success',
      alertText: action.payload.alertText
    }
  }

  if (action.type === SETUP_USER_ERROR) {
    return {
      ...state,
      isLoading: false,
      showAlert: true,
      alertType: 'danger',
      alertText: action.payload.msg,
    };
  }

  /* CREATE_USER */
  if (action.type === CREATE_JOB_BEGIN) {
    return { ...state, isLoading: true };
  }

  if (action.type === CREATE_JOB_SUCCESS) {
    return {
      ...state,
      isLoading: false,
      showAlert: true,
      alertType: 'success',
      alertText: 'New Job Created!',
    };
  }
  if (action.type === CREATE_JOB_ERROR) {
    return {
      ...state,
      isLoading: false,
      showAlert: true,
      alertType: 'danger',
      alertText: action.payload.msg,
    };
  }

  if (action.type === TOGGLE_SIDEBAR) {
    return {
      ...state,
      showSideBar: !state.showSideBar
    }
  }

  if (action.type === LOGOUT_USER) {
    return {
      ...initialState,
      token: '',
      fullName: '',
      location: '',
      age: ''

    }
  }
  if (action.type === SHOW_STATS_BEGIN) {
    return {
      ...state,
      isLoading: true,
      showAlert: false,
    };
  }
  if (action.type === SHOW_STATS_SUCCESS) {
    return {
      ...state,
      isLoading: false,
      stats: action.payload.stats,
    };
  }
  if (action.type === SHOW_STATS_ERROR) {
    return {
      ...state,
      isLoading: false,
      showAlert: true,
      alertType: 'danger',
      alertText: action.payload.msg,
    }
  }
  if (action.type === SHOW_MONTHLY_APPS_BEGIN) {
    return {
      ...state,
      isLoading: true,
      showAlert: false,
    };
  }
  if (action.type === SHOW_MONTHLY_APPS_SUCCESS) {
    return {
      ...state,
      isLoading: false,
      monthlyApplications: action.payload.monthlyApplications
    };
  }
  if (action.type === SHOW_MONTHLY_APPS_ERROR) {
    return {
      ...state,
      isLoading: false,
      showAlert: true,
      alertType: 'danger',
      alertText: action.payload.msg,
    };
  }
  if (action.type === GET_JOBS_BEGIN) {
    return {
      ...state,
      isLoading: true,
      showAlert: false,
    };
  }
  if (action.type === GET_JOBS_SUCCESS) {
    return {
      ...state,
      isLoading: false,
      jobs: action.payload.jobs,
      jobsFound: action.payload.jobsFound,
      numberOfJobs: action.payload.numberOfJobs,
      numberOfPages: action.payload.numberOfPages,
      totalNumberOfJobs: action.payload.totalNumberOfJobs
    };
  }
  if (action.type === GET_JOBS_ERROR) {
    return {
      ...state,
      isLoading: false,
      showAlert: true,
      alertType: 'danger',
      alertText: action.payload.msg,
    };
  }

  if (action.type === GET_JOBS_ON_SEARCH_BEGIN) {
    return {
      ...state,
      isLoading: true,
      showAlert: false,
    };
  }
  if (action.type === GET_JOBS_ON_SEARCH_SUCCESS) {
    return {
      ...state,
      isLoading: false,
      jobsOnSearch: action.payload.jobsOnSearch
    };
  }
  if (action.type === GET_JOBS_ON_SEARCH_ERROR) {
    return {
      ...state,
      isLoading: false,
      showAlert: true,
      alertType: 'danger',
      alertText: action.payload.msg,
    };
  }

  if (action.type === GET_USERS_ON_SEARCH_BEGIN) {
    return {
      ...state,
      isLoading: true,
      showAlert: false,
    };
  }
  if (action.type === GET_USERS_ON_SEARCH_SUCCESS) {
    return {
      ...state,
      isLoading: false,
      usersOnSearch: action.payload.usersOnSearch,
      isJobs: false
    };
  }
  if (action.type === GET_USERS_ON_SEARCH_ERROR) {
    return {
      ...state,
      isLoading: false,
      showAlert: true,
      alertType: 'danger',
      alertText: action.payload.msg,
    };
  }

  if (action.type === UPDATE_USER_BEGIN) {
    return { ...state, isLoading: true };
  }
  if (action.type === UPDATE_USER_SUCCESS) {
    return {
      ...state,
      isLoading: false,
      firstName: action.payload.firstName,
      lastName: action.payload.lastName,
      emailID: action.payload.emailID,
      password: action.payload.password,
      location: action.payload.location,
      age: action.payload.age,
      showAlert: true,
      alertType: 'success',
      alertText: 'User Profile Updated!',
    };
  }
  if (action.type === UPDATE_USER_ERROR) {
    return {
      ...state,
      isLoading: false,
      showAlert: true,
      alertType: 'danger',
      alertText: action.payload.msg,
    };
  }

  if (action.type === GET_ALL_USERS_BEGIN) {
    return {
      ...state,
      isLoading: true,
      showAlert: false,
    };
  }
  if (action.type === GET_ALL_USERS_SUCCESS) {
    return {
      ...state,
      isLoading: false,
      users: action.payload.users,
      isJobs: false,
      numberOfUsers: action.payload.numberOfUsers,
      numberOfPages: action.payload.numberOfPages,
      totalNumberOfUsers: action.payload.totalNumberOfUsers
    };
  }
  if (action.type === GET_ALL_USERS_ERROR) {
    return {
      ...state,
      isLoading: false,
      showAlert: true,
      alertType: 'danger',
      alertText: action.payload.msg,
    };
  }
  if (action.type === GET_USER_BEGIN) {
    return {
      ...state,
      isLoading: true,
      showAlert: false,
    };
  }
  if (action.type === GET_USER_SUCCESS) {
    return {
      ...state,
      isLoading: false,
      firstName: action.payload.firstName,
      lastName: action.payload.lastName,
      emailID: action.payload.emailID,
      location: action.payload.location,
      age: action.payload.age
    };
  }
  if (action.type === HANDLE_CHANGE) {
    return {
      ...state,
      [action.payload.name]: action.payload.value,
    };
  }

  if (action.type === CLEAR_FILTERS) {
    return {
      ...state,
      sortBy: 'jobId',
      search: '',
      searchKey: '',
      sort: 'userId'
    };
  }

  if (action.type === CHANGE_PAGE) {
    return {
      ...state,
      pageNumber: action.payload.pageNumber,
      pageSize: action.payload.pageSize
    };
  }

  if (action.type === CLEAR_VALUES) {
    const initialState = {
      isEditing: false,
      jobId: '',
      position: '',
      company: '',
      jobLocation: '',
      jobType: 'full-time',
      status: 'pending',
    };

    return {
      ...state,
      ...initialState,
    };
  }
  if (action.type === GET_CURRENT_USER_BEGIN) {
    return { ...state, userLoading: true, showAlert: false };
  }
  if (action.type === GET_CURRENT_USER_SUCCESS) {
    return {
      ...state,
      userLoading: false,
      user: action.payload.user,
    };
  }
  if (action.type === USERS_DATA_BEGIN) {
    return {
      ...state,
      isLoading: true,
      showAlert: false,
    };
  }
  if (action.type === USERS_DATA_SUCCESS) {
    return {
      ...state,
      isLoading: false,
      usersData: action.payload.usersData
    };
  }
  throw new Error(`no such action : ${action.type}`);
};

export default reducer;
