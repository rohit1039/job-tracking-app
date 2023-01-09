import React from 'react'
import { useEffect } from 'react';
import { useAppContext } from '../../context/appContext';
import Alert from '../../components/Alert';
import Loading from '../../components/Loading';
import Wrapper from '../../assets/wrappers/Job'
import PageBtnContainer from '../../components/PageBtnContainer';
import { FaFileCsv, FaFilePdf, FaFileExcel } from 'react-icons/fa'
import { Link } from 'react-router-dom';
import SearchContainer from "../../components/SearchContainer";

const AllUsers = () => {

  const { showAlert, getAllUsers, users, pageNumber, pageSize, sort, searchKey, usersOnSearch, totalNumberOfUsers, searchUsers, numberOfPages, isLoading, usersDataInFileFormat } = useAppContext();

  useEffect(() => {
    getAllUsers();
    searchUsers();
  }, [pageNumber, pageSize, sort, searchKey]);

  if (isLoading) {
    return <Loading center />
  }

  return (
    <>
      {searchKey ?
        <>
          <Wrapper>
            {showAlert && <Alert />}
            <div className='files'>
              <Link type="button" className='btn export-csv-file' onClick={() => usersDataInFileFormat({ fileType: 'csv' })}><FaFileCsv /> download csv</Link>
              <Link type="button" className='btn export-excel-file' onClick={() => usersDataInFileFormat({ fileType: 'excel' })}><FaFileExcel /> download excel</Link>
              <Link type="button" className='btn export-pdf-file' onClick={() => usersDataInFileFormat({ fileType: 'pdf' })}><FaFilePdf /> download pdf</Link>
            </div>
          </Wrapper>
          <SearchContainer />
          <h5 style={{ fontWeight: 'bold', marginLeft: '1rem' }}>
            {usersOnSearch?.length} user{usersOnSearch?.length > 1 && 's'} found
          </h5>
          <Wrapper>
            &nbsp;
            <div className="full-details">
              <table>
                <thead>
                  <tr>
                    <th>#</th>
                    <th className='hideable-column'>First Name</th>
                    <th className='hideable-column'>Last Name</th>
                    <th>Full Name</th>
                    <th>Username</th>
                    <th>Location</th>
                    <th>Age</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {usersOnSearch?.map((user, index) => {
                    return (
                      <tr key={index}>
                        <td>{user.userId}</td>
                        <td className='hideable-column'>{user.firstName}</td>
                        <td className='hideable-column'>{user.lastName}</td>
                        <td>{user.firstName + " " + user.lastName}</td>
                        <td>{user.emailID}</td>
                        <td>{user.location}</td>
                        <td>{user.age}</td>
                        <tr className='action'>
                          <td style={{ border: 'none' }}>
                            <div className='actions'>
                              <Link
                                to={``}
                                className='btn edit-btn'
                                onClick={""}
                              >
                                Edit
                              </Link>
                              <button
                                type='button'
                                className='btn delete-btn'
                                onClick={""}
                              >
                                Delete
                              </button>
                            </div>
                          </td>
                        </tr>
                      </tr>
                    )
                  })}
                </tbody>
              </table>
            </div>

            {/* for small screen */}
            <div className="less-details">
              <table>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Full Name</th>
                    <th>Location</th>
                  </tr>
                </thead>
                <tbody>
                  {usersOnSearch?.map((user, index) => {
                    return (
                      <tr key={index}>
                        <td>{user.userId}</td>
                        <td>{user.firstName + " " + user.lastName}</td>
                        <td>{user.location}</td>
                      </tr>
                    )
                  })}
                </tbody>
              </table>
            </div>
          </Wrapper>
        </>
        :
        <>
          <Wrapper>
            {showAlert && <Alert />}
            <div className='files'>
              <Link type="button" className='btn export-csv-file' onClick={() => usersDataInFileFormat({ fileType: 'csv' })}><FaFileCsv /> download csv</Link>
              <Link type="button" className='btn export-excel-file' onClick={() => usersDataInFileFormat({ fileType: 'excel' })}><FaFileExcel /> download excel</Link>
              <Link type="button" className='btn export-pdf-file' onClick={() => usersDataInFileFormat({ fileType: 'pdf' })}><FaFilePdf /> download pdf</Link>
            </div>
          </Wrapper>
          <SearchContainer />
          <h5 style={{ fontWeight: 'bold', marginLeft: '1rem' }}>
            {totalNumberOfUsers ? totalNumberOfUsers : usersOnSearch?.length} user{users?.length > 1 && 's'} found
          </h5>
          <Wrapper>
            &nbsp;
            <div className="full-details">
              <table>
                <thead>
                  <tr>
                    <th>#</th>
                    <th className='hideable-column'>First Name</th>
                    <th className='hideable-column'>Last Name</th>
                    <th>Full Name</th>
                    <th>Username</th>
                    <th>Location</th>
                    <th>Age</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {users?.map((user, index) => {
                    return (
                      <tr key={index}>
                        <td>{user.userId}</td>
                        <td className='hideable-column'>{user.firstName}</td>
                        <td className='hideable-column'>{user.lastName}</td>
                        <td>{user.firstName + " " + user.lastName}</td>
                        <td>{user.emailID}</td>
                        <td>{user.location}</td>
                        <td>{user.age}</td>
                        <tr className='action'>
                          <td style={{ border: 'none' }}>
                            <div className='actions'>
                              <Link
                                to={``}
                                className='btn edit-btn'
                                onClick={""}
                              >
                                Edit
                              </Link>
                              <button
                                type='button'
                                className='btn delete-btn'
                                onClick={""}
                              >
                                Delete
                              </button>
                            </div>
                          </td>
                        </tr>
                      </tr>
                    )
                  })}
                </tbody>
              </table>
              {numberOfPages > 1 && <PageBtnContainer />}
            </div>

            {/* for small screen */}
            <div className="less-details">
              <table>
                <thead>
                  <tr>
                    <th>#</th>
                    <th>Full Name</th>
                    <th>Location</th>
                  </tr>
                </thead>
                <tbody>
                  {users?.map((user, index) => {
                    return (
                      <tr key={index}>
                        <td>{user.userId}</td>
                        <td>{user.firstName + " " + user.lastName}</td>
                        <td>{user.location}</td>
                      </tr>
                    )
                  })}
                </tbody>
              </table>
              {numberOfPages > 1 && <PageBtnContainer />}
            </div>
          </Wrapper>
        </>
      }
    </>
  )
}
export default AllUsers;