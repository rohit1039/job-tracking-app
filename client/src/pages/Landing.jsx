import React from 'react';
import main from '../assets/images/job-seeker.png';
import { Wrapper } from '../assets/wrappers/Wrapper';
import { Link } from 'react-router-dom';
import { Logo } from '../components';
import { useAppContext } from '../context/appContext';

const Home = () => {

    const userId = useAppContext()

    return (
        <React.Fragment>
            {/* {userId && <Navigate to='/' />} */}
            <Wrapper>
                <nav>
                    <Logo />
                </nav>
                <div className='container page'>
                    <div className='info'>
                        <h1>job <span>tracking</span> app</h1>
                        <p>
                            Believe in yourself! Have faith in your abilities!
                            Without a humble but reasonable confidence in your own powers you cannot be successful or happy.
                        </p>
                        <Link to="/register" className='btn btn-hero'>
                            Login/Register
                        </Link>
                    </div>
                    <img src={main} alt="job hunt" className='img main-img' />
                </div>
            </Wrapper>
        </React.Fragment>
    )
}

export default Home;