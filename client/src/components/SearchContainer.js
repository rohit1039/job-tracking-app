import { FormRow, FormRowSelect } from '.';
import { useAppContext } from '../context/appContext';
import Wrapper from '../assets/wrappers/SearchContainer';
import { useMemo, useState } from 'react';

const SearchContainer = () => {

  const [localSearch, setLocalSearch] = useState('');

  const {
    isLoading,
    sortBy,
    sortOptions,
    sort,
    isJobs,
    sortOptionsForUser,
    handleChange,
    clearFilters
  } = useAppContext();

  const handleSearch = (e) => {
    handleChange({ name: e.target.name, value: e.target.value });
  };
  const handleSubmit = (e) => {
    e.preventDefault();
    setLocalSearch('');
    clearFilters();
  };
  const debounce = () => {
    let timeoutID;
    return (e) => {
      setLocalSearch(e.target.value);
      clearTimeout(timeoutID);
      timeoutID = setTimeout(() => {
        handleChange({ name: e.target.name, value: e.target.value });
      }, 1000);
    };
  };
  const optimizedDebounce = useMemo(() => debounce(), []);

  return (
    <Wrapper>
      <form className='form'>
        <h4>search form</h4>
        <div className='form-center'>

          {isJobs ?
            <FormRow
              type='text'
              name='search'
              value={localSearch}
              handleChange={optimizedDebounce}
            />
            :
            <FormRow
              type='text'
              name='searchKey'
              value={localSearch}
              handleChange={optimizedDebounce}
            />
          }

          {isJobs ?
            <FormRowSelect
              name='sortBy'
              value={sortBy}
              handleChange={handleSearch}
              list={sortOptions}
            />
            :
            <FormRowSelect
              name='sort'
              value={sort}
              handleChange={handleSearch}
              list={sortOptionsForUser}
            />
          }
          <button
            className='btn btn-block btn-danger'
            disabled={isLoading}
            onClick={handleSubmit}
          >
            clear filters
          </button>
        </div>
      </form>
    </Wrapper>
  );
};

export default SearchContainer;