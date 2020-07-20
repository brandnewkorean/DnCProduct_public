package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jdbcUtil.CatBoardDAO;
import vo.CatBoardVO;
import vo.PageVO;

@Service
public class CatBoardServiceImpl implements CatBoardService {
	
	@Autowired
	CatBoardDAO dao;
	
	@Override
	public List<CatBoardVO> selectList(){
		return dao.selectList();
	} // selectList
	
	
	@Override
	public CatBoardVO selectOne(CatBoardVO bv) {
		return dao.selectOne(bv);
	} // selectOne
	
	@Override
	public int insert(CatBoardVO bv) {
		return dao.insert(bv);
	} // insert
	
	@Override
	public int countUp(CatBoardVO bv) {
		return dao.countUp(bv);
	}
	
	@Override
	public int update(CatBoardVO bv) {
		return dao.update(bv);
	}
	
	@Override
	public int delete(CatBoardVO bv) {
		return dao.delete(bv);
	}
	
	@Override
	public PageVO<CatBoardVO> pageList(PageVO<CatBoardVO> pvo) {
		return dao.pageList(pvo);
	}
} // CatBoardServiceImpl