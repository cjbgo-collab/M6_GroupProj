package m6.project.model;

import jakarta.persistence.*;

@Entity
@Table(name = "loans")
public class Loan { 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer loanId;

    private String loanBookTitle;
    private String borrowerName;
    private Integer loanBookId;

  
    public Integer getLoanId() { return loanId; }
    public void setLoanId(Integer loanId) { this.loanId = loanId; }

    public String getLoanBookTitle() { return loanBookTitle; }
    public void setLoanBookTitle(String loanBookTitle) { this.loanBookTitle = loanBookTitle; }

    public String getBorrowerName() { return borrowerName; }
    public void setBorrowerName(String borrowerName) { this.borrowerName = borrowerName; }

    public Integer getLoanBookId() { return loanBookId; }
    public void setLoanBookId(Integer loanBookId) { this.loanBookId = loanBookId; }
}
