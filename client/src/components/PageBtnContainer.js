import { useAppContext } from '../context/appContext'
import { HiChevronDoubleLeft, HiChevronDoubleRight } from 'react-icons/hi'
import Wrapper from '../assets/wrappers/PageBtnContainer'

const PageBtnContainer = () => {
  const { numberOfPages, pageNumber, pageSize, changePage } = useAppContext()

  const pages = Array.from({ length: numberOfPages }, (_, index) => {
    return index + 1
  })
  const nextPage = () => {
    let newPage = pageNumber + 1
    if (newPage > numberOfPages) {
      newPage = 1
    }
    changePage(newPage, pageSize)
  }
  const prevPage = () => {
    let newPage = pageNumber - 1
    if (newPage < 1) {
      newPage = numberOfPages
    }
    changePage(newPage, pageSize)
  }
  return (
    <Wrapper>
      <button className='prev-btn' onClick={prevPage}>
        <HiChevronDoubleLeft />
        prev
      </button>
      <div className='btn-container'>
        {pages.map((page) => {
          return (
            <button
              type='button'
              className={page === pageNumber ? 'pageBtn active' : 'pageBtn'}
              key={page}
              onClick={() => changePage(page, pageSize)}
            >
              {page}
            </button>
          )
        })}
      </div>
      <button className='next-btn' onClick={nextPage}>
        next
        <HiChevronDoubleRight />
      </button>
    </Wrapper>
  )
}

export default PageBtnContainer