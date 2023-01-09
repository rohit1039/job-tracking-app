import { useEffect } from 'react';
import { useAppContext } from '../context/appContext'
import Job from './Job';
import Wrapper from '../assets/wrappers/JobsContainer';
import Alert from '../components/Alert';
import Loading from './Loading';
import PageBtnContainer from './PageBtnContainer';

const JobsContainer = () => {

    const { allJobs, searchJobs, pageNumber, pageSize, sortBy,
        jobs, jobsOnSearch, totalNumberOfJobs, search, numberOfPages,
        showAlert, isLoading } = useAppContext();

    useEffect(() => {
        allJobs();
        searchJobs();
        // eslint-disable-next-line
    }, [pageNumber, pageSize, sortBy, search]);

    if (isLoading) {
        return <Loading center />
    }

    if (jobs?.length === 0) {
        return (
            <Wrapper>
                <h2>No jobs to display...</h2>
            </Wrapper>
        );
    }

    return (
        <>
            {search ?
                <Wrapper>
                    {showAlert && <Alert />}
                    <h5>
                        {jobsOnSearch?.length} job{jobsOnSearch?.length > 1 && 's'} found
                    </h5>
                    <div className='jobs'>
                        {jobsOnSearch?.map((job, index) => {
                            return <Job key={index} {...job} />
                        })}
                    </div>
                </Wrapper>
                :
                <Wrapper>
                    {showAlert && <Alert />}
                    <h5>
                        {totalNumberOfJobs} job{jobs?.length > 1 && 's'} found
                    </h5>
                    <div className='jobs'>
                        {jobs?.map((job, index) => {
                            return <Job key={index} {...job} />
                        })}
                    </div>
                    {numberOfPages > 1 && <PageBtnContainer />}
                </Wrapper>
            }
        </>
    )
}
export default JobsContainer;