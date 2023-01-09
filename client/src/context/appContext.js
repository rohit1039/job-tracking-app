import React, { useReducer, useContext, useEffect } from 'react';

import reducer from './reducer';
import {
  DISPLAY_ALERT, CLEAR_ALERT, SETUP_USER_SUCCESS,
  SETUP_USER_ERROR, SETUP_USER_BEGIN, TOGGLE_SIDEBAR,
  LOGOUT_USER, UPDATE_USER_SUCCESS, UPDATE_USER_ERROR,
  UPDATE_USER_BEGIN, CREATE_JOB_BEGIN, CREATE_JOB_SUCCESS,
  CREATE_JOB_ERROR, EDIT_JOB_BEGIN, EDIT_JOB_SUCCESS, EDIT_JOB_ERROR,
  CLEAR_VALUES, HANDLE_CHANGE, SET_EDIT_JOB, GET_JOBS_SUCCESS, GET_JOBS_ERROR, GET_JOBS_BEGIN, SHOW_STATS_BEGIN, SHOW_STATS_SUCCESS, SHOW_MONTHLY_APPS_BEGIN, SHOW_MONTHLY_APPS_SUCCESS, GET_ALL_USERS_BEGIN, GET_ALL_USERS_SUCCESS, GET_ALL_USERS_ERROR, GET_CURRENT_USER_SUCCESS, GET_CURRENT_USER_BEGIN, CLEAR_FILTERS, CHANGE_PAGE, USERS_DATA_PDF_SUCCESS, USERS_DATA_PDF_BEGIN, GET_USER_BEGIN, GET_USER_SUCCESS, SHOW_MONTHLY_APPS_ERROR, SHOW_STATS_ERROR, USERS_DATA_BEGIN, USERS_DATA_SUCCESS, USER_CHANGE_PAGE, GET_JOBS_ON_SEARCH_ERROR, GET_JOBS_ON_SEARCH_SUCCESS, GET_JOBS_ON_SEARCH_BEGIN, GET_USERS_ON_SEARCH_BEGIN, GET_USERS_ON_SEARCH_SUCCESS, GET_USERS_ON_SEARCH_ERROR
} from './actions';
import axios from 'axios';

const token = localStorage.getItem("token")
const fullName = localStorage.getItem("fullName")
const location = localStorage.getItem("location")
const firstName = localStorage.getItem("firstName")
const lastName = localStorage.getItem("lastName")
const emailID = localStorage.getItem("emailID")
const age = localStorage.getItem("age")

const initialState = {
  userLoading: false,
  isLoading: false,
  isEditing: false,
  isJobs: true,
  pageNumber: 1,
  pageSize: 10,
  showAlert: false,
  alertText: '',
  alertType: '',
  createdAt: '',
  position: '',
  firstName: firstName,
  lastName: lastName,
  emailID: emailID,
  token: token,
  company: '',
  jobLocation: '',
  fullName: fullName,
  jobTypeOptions: ['FULL-TIME', 'PART-TIME', 'REMOTE', 'INTERNSHIP'],
  jobType: 'FULL-TIME',
  statusOptions: ['INTERVIEW', 'PENDING', 'DECLINED'],
  status: 'PENDING',
  password: '',
  location: location,
  age: age,
  showSideBar: false,
  sortBy: 'jobId',
  sortOptions: ['jobId', 'company', 'position', 'jobLocation'],
  sort: 'userId',
  sortOptionsForUser: ['userId', 'location', 'emailID'],
  search: '',
  searchKey: '',
};

const AppContext = React.createContext();

const AppProvider = ({ children }) => {
  const [state, dispatch] = useReducer(reducer, initialState);

  const myAxios = axios.create({
    baseURL: '/jobify'
  })

  const privateAxios = axios.create({
    baseURL: '/jobify', headers: {
      Authorization: `Bearer ${state.token}`
    }
  });

  const displayAlert = () => {
    dispatch({ type: DISPLAY_ALERT });
    clearAlert();
  };

  const clearAlert = () => {
    setTimeout(() => {
      dispatch({ type: CLEAR_ALERT });
    }, 3000);
  };

  const timeOut = () => {
    setTimeout(() => {
      logoutUser();
    }, 3000)

  }

  const addDataToLocalStorage = ({ token, fullName, firstName, lastName, userId, location, age, emailID }) => {
    localStorage.setItem("location", location)
    localStorage.setItem("age", age)
    localStorage.setItem("userId", userId)
    localStorage.setItem("emailID", emailID)
    localStorage.setItem("token", token)
    localStorage.setItem("firstName", firstName)
    localStorage.setItem("lastName", lastName)
    localStorage.setItem("fullName", fullName)
  }

  const userId = localStorage.getItem("userId")

  const removeDataFromLocalStorage = () => {
    localStorage.removeItem("token")
    localStorage.removeItem("userId")
    localStorage.removeItem("fullName")
    localStorage.removeItem("firstName")
    localStorage.removeItem("lastName")
    localStorage.removeItem("location")
    localStorage.removeItem("emailID")
    localStorage.removeItem("age")
  }

  const toggleSideBar = () => {
    dispatch({
      type: TOGGLE_SIDEBAR
    })
  }

  const logoutUser = () => {
    dispatch({
      type: LOGOUT_USER
    })
    removeDataFromLocalStorage()
  }

  /* Create Job */
  const createJob = async () => {
    dispatch({ type: CREATE_JOB_BEGIN });
    try {
      const { position, company, jobLocation, jobType, status, createdAt } = state;
      await privateAxios.post(`/jobs/create/${userId}`, {
        position,
        company,
        jobLocation,
        jobType,
        status,
        createdAt
      });
      dispatch({ type: CREATE_JOB_SUCCESS });
      dispatch({ type: CLEAR_VALUES });
    } catch (error) {
      if (error.response.status === 401) return;
      dispatch({
        type: CREATE_JOB_ERROR,
        payload: { msg: error.response.data.message }
      });
    }
    clearAlert();
  };

  /** Update User */
  const updateUser = async (currentUser, alertText) => {
    dispatch({ type: UPDATE_USER_BEGIN });
    try {
      const { data } = await privateAxios.put(`/users/update/${userId}`, currentUser
      );
      const { firstName, lastName, emailID, password, location, age } = data;
      dispatch({
        type: UPDATE_USER_SUCCESS,
        payload: { firstName, lastName, emailID, password, location, age, alertText }
      });
      dispatch({ type: CLEAR_VALUES });
    } catch (error) {
      console.log(error)
      dispatch({
        type: UPDATE_USER_ERROR,
        payload: { msg: error.response.data.message }
      });
    }
    clearAlert();
    timeOut();
  };

  /** Get user by userId */
  const userByUserId = async () => {
    dispatch({ type: GET_USER_BEGIN });
    try {
      const { data } = await myAxios.get(`/users/get/user/${userId}`
      );
      const { firstName, lastName, emailID, location, age } = data;
      dispatch({
        type: GET_USER_SUCCESS,
        payload: { firstName, lastName, emailID, location, age }
      });
    } catch (error) {
      console.log(error);
    }
    clearAlert();
  }

  const handleChange = ({ name, value }) => {
    dispatch({ type: HANDLE_CHANGE, payload: { name, value } });
  };

  const clearValues = () => {
    dispatch({ type: CLEAR_VALUES });
  };

  /** Export user's data */
  const usersDataInFileFormat = async ({ fileType }) => {
    try {
      dispatch({ type: USERS_DATA_BEGIN })
      myAxios({
        url: `/users/export/${fileType}`,
        method: 'GET',
        responseType: 'blob',
      }).then((response) => {
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        fileType === "excel" ? link.setAttribute('download', 'users_data.xlsx') : link.setAttribute('download', `users_data.${fileType}`);
        document.body.appendChild(link);
        link.click();
        dispatch({
          type: USERS_DATA_SUCCESS,
          payload: { usersData: response.data }
        })
      })
    } catch (error) {
      console.log(error);
    }
  }

  /** Show Stats */
  const showStats = async () => {
    try {
      dispatch({ type: SHOW_STATS_BEGIN })
      const { data } = await myAxios.get(`/jobs/stats/user/${userId}`)
      dispatch({
        type: SHOW_STATS_SUCCESS,
        payload: { stats: data }
      })
    } catch (error) {
      if (error.response.status === 404) {
        dispatch({
          type: SHOW_STATS_ERROR,
          payload: { msg: error.response.data.message }
        })
      }
      //logoutUser();
    }
  }

  /** Monthly Applications */
  const showMonthlyApps = async () => {
    dispatch({ type: SHOW_MONTHLY_APPS_BEGIN })
    try {
      const { data } = await myAxios.get(`/jobs/monthly-apps/user/${userId}`)
      const { monthlyApplications } = data
      dispatch({
        type: SHOW_MONTHLY_APPS_SUCCESS,
        payload: {
          monthlyApplications
        },
      })
    } catch (error) {
      if (error.response.status === 404) {
        dispatch({
          type: SHOW_MONTHLY_APPS_ERROR,
          payload: { msg: error.response.data.message }
        })
      }
      //logoutUser();
    }
  }

  /** Search jobs */
  const searchJobs = async () => {
    const { search } = state;
    dispatch({
      type: GET_JOBS_ON_SEARCH_BEGIN
    })
    try {
      const { data } = await myAxios.get(`/jobs/search?search=${search}`);
      const { jobsOnSearch } = data;
      dispatch({
        type: GET_JOBS_ON_SEARCH_SUCCESS,
        payload: { jobsOnSearch }
      })
    } catch (error) {
      dispatch({
        type: GET_JOBS_ON_SEARCH_ERROR,
        payload: { msg: error.response.data.message },
      });
      console.log(error)
    }
    clearAlert()
  };

  /** Search jobs */
  const searchUsers = async () => {
    const { searchKey, isJobs } = state;
    dispatch({
      type: GET_USERS_ON_SEARCH_BEGIN
    })
    try {
      const { data } = await myAxios.get(`/users/search?search=${searchKey}`);
      const { usersOnSearch } = data;
      dispatch({
        type: GET_USERS_ON_SEARCH_SUCCESS,
        payload: { usersOnSearch, isJobs }
      })
    } catch (error) {
      dispatch({
        type: GET_USERS_ON_SEARCH_ERROR,
        payload: { msg: error.response.data.message },
      });
      console.log(error)
    }
    clearAlert()
  };


  /** Get list of jobs */
  const allJobs = async () => {
    const { pageNumber, pageSize, sortBy } = state;
    dispatch({
      type: GET_JOBS_BEGIN
    })
    try {
      const { data } = await myAxios.get(`/jobs/get/all?pageNumber=${pageNumber}&pageSize=${pageSize}&sortBy=${sortBy}`);
      const { jobs, numberOfPages, numberOfJobs, totalNumberOfJobs } = data;
      dispatch({
        type: GET_JOBS_SUCCESS,
        payload: { jobs, numberOfPages, numberOfJobs, totalNumberOfJobs }
      })
    } catch (error) {
      dispatch({
        type: GET_JOBS_ERROR,
        payload: { msg: error.response.data.message },
      });
      console.log(error)
    }
    clearAlert()
  };

  /** Get list of users */
  const getAllUsers = async () => {
    const { pageNumber, pageSize, sort, isJobs } = state;
    dispatch({
      type: GET_ALL_USERS_BEGIN
    })
    try {
      const { data } = await myAxios.get(`/users/all?pageNumber=${pageNumber}&pageSize=${pageSize}&sortBy=${sort}`);
      const { users, numberOfPages, numberOfUsers, totalNumberOfUsers } = data
      dispatch({
        type: GET_ALL_USERS_SUCCESS,
        payload: {
          users, numberOfPages, numberOfUsers, totalNumberOfUsers, isJobs
        }
      })
    } catch (error) {
      dispatch({
        type: GET_ALL_USERS_ERROR,
        payload: { msg: error.response.data.message }
      })
      console.log(error);
    }
    clearAlert();
  };

  /** Login and Register */
  const setupUser = async ({ currentUser, endPoint, alertText }) => {
    dispatch({ type: SETUP_USER_BEGIN });
    try {
      const { data } = await myAxios.post(
        `/users/${endPoint}`,
        currentUser
      );
      const { userId, firstName, lastName, emailID, password, location, age, token, fullName } = data;
      dispatch({
        type: SETUP_USER_SUCCESS,
        payload: { userId, firstName, lastName, emailID, password, location, age, token, fullName, alertText }
      });
      dispatch({ type: CLEAR_VALUES });
      addDataToLocalStorage({ token, fullName, firstName, lastName, userId, location, age, emailID })
    } catch (error) {
      if (error.response.data.message) {
        dispatch({
          type: SETUP_USER_ERROR,
          payload: { msg: error.response.data.message },
        });
      }
      else if (error.response.status === 409) {
        dispatch({
          type: SETUP_USER_ERROR,
          payload: { msg: error.response.data },
        });
      }
      else if (error.response.status === 400 && error.response.data.password) {
        dispatch({
          type: SETUP_USER_ERROR,
          payload: { msg: "Password: " + error.response.data.password }
        });
      }
      else if (error.response.status === 400 && error.response.data.age) {
        dispatch({
          type: SETUP_USER_ERROR,
          payload: { msg: "Age: " + error.response.data.age }
        });
      }
      else {
        dispatch({
          type: SETUP_USER_ERROR,
          payload: { msg: "Something went wrong on server!" },
        });
      }
    }
    clearAlert();
  };

  /* Edit Job */
  const setEditJob = (jobId) => {
    dispatch({ type: SET_EDIT_JOB, payload: { jobId } });
  };
  const editJob = async () => {
    dispatch({ type: EDIT_JOB_BEGIN });
    try {
      const { position, company, jobLocation, jobType, status } = state;
      await privateAxios.put(`/jobs/${state.editJobId}`, {
        company,
        position,
        jobLocation,
        jobType,
        status,
      });
      dispatch({ type: EDIT_JOB_SUCCESS });
      dispatch({ type: CLEAR_VALUES });
    } catch (error) {
      if (error.response.status === 401) return;
      dispatch({
        type: EDIT_JOB_ERROR,
        payload: { msg: error.response.data.message },
      });
    }
    clearAlert();
  };

  /** Current LoggedIn User */
  const getCurrentUser = async () => {
    dispatch({ type: GET_CURRENT_USER_BEGIN });
    try {
      const { data } = await privateAxios('/jobs/current/user');
      const { user } = data;

      dispatch({
        type: GET_CURRENT_USER_SUCCESS,
        payload: { user }
      });
    } catch (error) {
      if (error.response.status === 401) return;
      console.log(error);
    }
  };
  useEffect(() => {
    getCurrentUser();
  }, []);

  const clearFilters = () => {
    dispatch({ type: CLEAR_FILTERS });
  };

  /** Handle change page */
  const changePage = (pageNumber, pageSize) => {
    dispatch({ type: CHANGE_PAGE, payload: { pageNumber, pageSize } });
  };

  return (
    <AppContext.Provider
      value={{
        ...state,
        displayAlert,
        setupUser,
        toggleSideBar,
        logoutUser,
        createJob,
        editJob,
        showStats,
        setEditJob,
        updateUser,
        handleChange,
        clearValues,
        changePage,
        clearFilters,
        usersDataInFileFormat,
        allJobs,
        searchJobs,
        searchUsers,
        userByUserId,
        getAllUsers,
        showMonthlyApps
      }}
    >
      {children}
    </AppContext.Provider>
  );
};

const useAppContext = () => {
  return useContext(AppContext);
};

export { AppProvider, initialState, useAppContext };