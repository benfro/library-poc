package net.benfro.library.core.domain.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import net.benfro.library.core.domain.Loan;
import net.benfro.library.core.repository.DbLoan;

@Mapper
public interface LoanMapper {

    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    @Named("toValue")
    default int toValue(Loan.Type loan) {
        return loan.getValue();
    }

    @Named("fromValue")
    default Loan.Type fromValue(int value) {
        return Loan.Type.CUSTOM.fromValue(value);
    }

    @Mapping(source = "type", target = "type", qualifiedByName = "toValue")
    DbLoan toDbLoan(Loan loan);

    @Mapping(source = "type", target = "type", qualifiedByName = "fromValue")
    Loan toLoan(DbLoan loan);


}
