/**
 * 
 */
package com.aia.eservice.common.utility;

import java.util.ArrayList;
import java.util.List;

/**
 * @author NSNP644
 *
 */
public class PageUtil<T> {

	public void getPage(List<T> list,Page<T> page)
	{
		//Page<T> result = new Page<T>();
		int pageSize = page.getPageSize();
		int currentPageNum = page.getCurrentPageIndex();
		int recorderTotalAmount = page.getTotalRecordsAmount();
		//获取总页数
		page.setTotalPageAmount(getPageTotalAmount(recorderTotalAmount,pageSize));
		//获取是否有下一页
		page.setIsHasNextPage(getIsHasNextPage(currentPageNum,page.getTotalPageAmount()));
		//获取下一页
		if(page.getIsHasNextPage()==1)
		{
			page.setNextPageIndex(getNextPageIndex(currentPageNum,page.getTotalPageAmount()));
			
		}
		//获取是否有前一页
		page.setIsHasPrevPage(getIsHasPrevPage(page.getCurrentPageIndex()));
		//前一页
		if(page.getIsHasPrevPage()==1)
		{
			page.setPrevPageIndex(getPrevPageIndex(page.getCurrentPageIndex()));
		}
		//
		if(page.getTotalPageAmount()==1)
		{
			page.setList(list);
			
		}
		else if((page.getCurrentPageIndex())>=page.getTotalPageAmount())
		{
			page.setList(new ArrayList<T>(list.subList((page.getCurrentPageIndex()-1)*pageSize,page.getTotalRecordsAmount() )));
		}
		else
		{
			page.setList(new ArrayList<T>(list.subList((page.getCurrentPageIndex()-1)*pageSize, page.getCurrentPageIndex()*pageSize)));
			
		}
		
		//return result;
	}
	
	public List<Page<T>> getPageList(List<T> list,int pageSize)
	{
		List<Page<T>> resultList = new ArrayList<Page<T>>();
		if(list==null ||list.isEmpty())
		{
			return resultList;			
		}
		int totalRecordAmount = list.size();
		int totalPageAmount = getPageTotalAmount(totalRecordAmount,pageSize);
		
		Page<T> page = null;
		for(int i = 0;i<totalPageAmount;i++)
		{
			page = new Page<T>();
			
			page.setPageSize(pageSize);
			page.setTotalRecordsAmount(totalRecordAmount);
			page.setTotalPageAmount(totalPageAmount);			
			page.setCurrentPageIndex((1+i));
			//取是否有下一页
			page.setIsHasNextPage(getIsHasNextPage(page.getCurrentPageIndex(),totalPageAmount));
			//获取下一页
			page.setNextPageIndex(getNextPageIndex(page.getCurrentPageIndex(),totalPageAmount));
			//获取是否有前一页
			page.setIsHasPrevPage(getIsHasPrevPage(page.getCurrentPageIndex()));
			//前一页
			page.setPrevPageIndex(getPrevPageIndex(page.getCurrentPageIndex()));
			if(totalPageAmount==1)
			{
				page.setList(list);
				break;
			}
			if((i+1)<totalPageAmount)
			{
				page.setList(new ArrayList<T>(list.subList((i-1)*pageSize, i*pageSize-1)));
			}
			else
			{
				page.setList(new ArrayList<T>(list.subList((i-1)*pageSize,totalRecordAmount-1 )));
			}
		}
		
		return resultList;	
	}
	
	/**
	 * 获取总页数
	 * @recordTotal 总记录数
	 * @pageSize 每页记录数
	 * 
	 * **/
	public static int getPageTotalAmount(int recordTotal,int pageSize)
	{
		int totalPages= recordTotal/pageSize;
		return (recordTotal%pageSize ==0? totalPages: (++totalPages));
		
	}
	
	/**
	 * 获取是否有下一页
	 * @currentPageIndex 当前页索引值
	 * @pageTotalAmount 总页数
	 * 
	 * **/
	public static int getIsHasNextPage(int currentPageIndex,int pageTotalAmount)
	{
		return (currentPageIndex<pageTotalAmount?1:0);
		
	}
	
	/**
	 * 获取下一页
	 * @currentPageIndex 当前页索引值
	 * @pageTotalAmount 总页数
	 * 
	 * **/
	public static int getNextPageIndex(int currentPageIndex,int pageTotalAmount)
	{
		if(getIsHasNextPage(currentPageIndex,pageTotalAmount)==1)
		{
			return ++currentPageIndex;
		}
		return currentPageIndex;
		
	}
	
	/**
	 * 获取是否有前一页
	 * @currentPageIndex 当前页索引值
	 * 
	 * 
	 * **/
	public static int getIsHasPrevPage(int currentPageIndex)
	{
		return (currentPageIndex>1?1:0);
		
	}
	
	/**
	 * 获取前一页
	 * @currentPageIndex 当前页索引值
	 * 
	 * 
	 * **/
	public static int getPrevPageIndex(int currentPageIndex)
	{
		if(getIsHasPrevPage(currentPageIndex)==1)
		{
			return --currentPageIndex;
		}
		return currentPageIndex;
	}
	
	
}
