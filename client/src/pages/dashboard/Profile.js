import { useEffect, useState } from 'react'
import { FormRow, Alert } from '../../components'
import Wrapper from '../../assets/wrappers/DashboardFormPage'
import { useAppContext } from '../../context/appContext'
import { BiBadgeCheck } from 'react-icons/bi'

import user_1 from '../../assets/images/user_1.jpg'
import user_2 from '../../assets/images/user_2.jpg'
import user_3 from '../../assets/images/user_3.jpg'
import user_4 from '../../assets/images/user_4.jpg'
import user_5 from '../../assets/images/user_5.jpg'
import { useParams } from 'react-router-dom'
import Loading from '../../components/Loading'

const Profile = () => {

  const { showAlert,
    displayAlert,
    updateUser,
    isLoading,
    fullName,
    userByUserId,
    firstName: fname,
    lastName: lname,
    location: userLocation,
    age: userAge,
    emailID: username } =
    useAppContext()

  useEffect(() => {
    userByUserId()
  },[])

  const userId = useParams()

  const [firstName, setFirstName] = useState()
  const [emailID, setEmailID] = useState()
  const [lastName, setLastName] = useState()
  const [location, setLocation] = useState()
  const [password, setPassword] = useState()
  const [age, setAge] = useState()

  const handleSubmit = (e) => {
    e.preventDefault()
    if (!firstName || !emailID || !lastName || !location) {
      displayAlert()
      return
    }
    updateUser({ firstName, emailID, lastName, location, age, password }, userId)
  }

  if(isLoading)
  {
    return(<Loading center/>)
  }

  return (
    <>
      <Wrapper>
        <form className='form' onSubmit={handleSubmit}>
          <h3>profile update</h3>
          {showAlert && <Alert />}
          <div className='form-center'>
            <FormRow
              type='text'
              name='first name'
              value={firstName}
              handleChange={(e) => setFirstName(e.target.value)}
            />
            <FormRow
              type='text'
              labelText='last name'
              name='lastName'
              value={lastName}
              handleChange={(e) => setLastName(e.target.value)}
            />
            <FormRow
              type='password'
              labelText='password'
              name='password'
              value={password}
              handleChange={(e) => setPassword(e.target.value)}
            />
            <FormRow
              type='number'
              labelText='age'
              name='age'
              value={age}
              handleChange={(e) => setAge(e.target.value)}
            />
            <FormRow
              type='email'
              name='emailID'
              value={emailID}
              handleChange={(e) => setEmailID(e.target.value)}
            />
            <FormRow
              type='text'
              name='location'
              value={location}
              handleChange={(e) => setLocation(e.target.value)}
            />
            <button className='btn btn-block' type='submit' disabled={isLoading}>
              {isLoading ? 'Please Wait...' : 'save changes'}
            </button>
          </div>
        </form>
      </Wrapper>
      <br />
      <h3 style={{ textAlign: 'center' }}>User's profile</h3>
      <Wrapper>
        <div className='profile_img'>
          <div className='pro_fields'>
            <h5>FirstName: {fname}</h5>
            <h5>LastName: {lname}</h5>
            <h5>FullName: {fname + " " + lname}</h5>
            <h5 style={{ display: 'inline-block' }}>Username:</h5>  <h5 style={{ textTransform: 'lowercase', display: 'inline-block' }}>{username}</h5>
            <h5>Location: {userLocation}</h5>
            <h5>Age: {userAge}</h5>
          </div>
          {
            new RegExp("^[a-e]").test(fullName.toLowerCase()) ?
              <img src={user_1} alt='user_1' className='pro_img' />
              : ''
          }
          {
            new RegExp("^[f-j]").test(fullName.toLowerCase()) ?
              <img src={user_2} alt='user_2' className='pro_img' />
              : ''
          }
          {
            new RegExp("^[k-o]").test(fullName.toLowerCase()) ?
              <img src={user_3} alt='user_3' className='pro_img' />
              : ''
          }
          {
            new RegExp("^[p-t]").test(fullName.toLowerCase()) ?
              <img src={user_4} alt='user_4' className='pro_img' />
              : ''
          }
          {
            new RegExp("^[u-z]").test(fullName.toLowerCase()) ?
              <img src={user_5} alt='user_5' className='pro_img' />
              : ''
          }
        </div>
      </Wrapper>
    </>
  )
}

export default Profile

